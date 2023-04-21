package net.sf.josceleton.connection.impl.osc;

import net.sf.josceleton.commons.exception.InvalidArgumentException;
import net.sf.josceleton.commons.test.jmock.AbstractMockeryTest;
import net.sf.josceleton.connection.impl.service.user.UserStore;
import net.sf.josceleton.connection.impl.test.TestableOSCMessage;
import net.sf.josceleton.core.api.entity.joint.Joint;
import net.sf.josceleton.core.api.entity.location.Coordinate;
import net.sf.josceleton.core.api.entity.message.JointMessage;
import net.sf.josceleton.core.api.entity.message.UserMessage;
import net.sf.josceleton.core.api.entity.user.User;
import net.sf.josceleton.core.api.entity.user.UserState;
import net.sf.josceleton.core.impl.entity.FactoryFacade;
import net.sf.josceleton.core.impl.entity.user.UserFactory;
import org.jmock.Expectations;
import org.testng.annotations.Test;
import com.illposed.osc.OSCMessage;

@SuppressWarnings("boxing")
public class OscMessageTransformerImplTest extends AbstractMockeryTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*arguments.length.*3.*")
    public final void tooLessJointOscMessageArgumentsFails() {
        final OscMessageTransformer transformer = this.newSimpleTransformer();
        final OSCMessage oscMessage = TestableOSCMessage.newMockSafeArguments(this.getMockery(), new Object[] { 1, 2, 3 });
        transformer.transformJointMessage(oscMessage, this.mock(UserStore.class));
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = "Passed an illegal argument \\[oscMessage.arguments.length\\] with value: " + "\\[0\\]! \\(condition was: ==1\\)")
    public final void passingZeroArgumentedOSCMessageFails() {
        final OscMessageTransformer transformer = this.newSimpleTransformer();
        final OSCMessage oscMessage = TestableOSCMessage.newMockSafeArguments(this.getMockery(), new Object[] {});
        transformer.transformUserMessage(oscMessage, this.mock(UserStore.class));
    }

    private OscMessageTransformer newSimpleTransformer() {
        final FactoryFacade factory = this.mock(FactoryFacade.class);
        return new OscMessageTransformerImpl(factory);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Invalid joint ID \\[x_hand\\]!")
    public final void transformJointMessageInvalidJointIdFails() {
        final float[] unusedCoorindates = new float[] { 0.0F, 0.0F, 0.0F };
        final Integer osceletonUserId = Integer.valueOf(3);
        final OscMessageTransformer transformer = this.newTransformer(osceletonUserId, null, null, unusedCoorindates);
        final OSCMessage oscMessage = this.newOSCMessage(osceletonUserId, "x_hand", null, unusedCoorindates);
        transformer.transformJointMessage(oscMessage, this.mock(UserStore.class));
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Invalid user message address \\[\\/invalid\\]!")
    public final void transformUserMessageInvalidAddressFails() {
        final float[] unusedCoorindates = new float[] { 0.0F, 0.0F, 0.0F };
        final Integer osceletonUserId = Integer.valueOf(3);
        final OscMessageTransformer transformer = this.newTransformer(osceletonUserId, null, null, unusedCoorindates);
        final OSCMessage oscMessage = this.newOSCMessage(osceletonUserId, null, "/invalid", unusedCoorindates);
        transformer.transformUserMessage(oscMessage, this.mock(UserStore.class));
    }

    private OSCMessage newOSCMessage(final Integer osceletonUserId, final String osceletonJointId, final String userState, final float[] coordinates) {
        final OSCMessage oscMessage = this.mock(OSCMessage.class);
        final Object[] oscArguments;
        if (osceletonJointId != null) {
            oscArguments = new Object[] { osceletonJointId, osceletonUserId, coordinates[0], coordinates[1], coordinates[2] };
        } else {
            oscArguments = new Object[] { osceletonUserId };
        }
        this.checking(new Expectations() {

            {
                oneOf(oscMessage).getArguments();
                will(returnValue(oscArguments));
                if (userState != null) {
                    oneOf(oscMessage).getAddress();
                    will(returnValue(userState));
                }
            }
        });
        return oscMessage;
    }

    private OscMessageTransformer newTransformer(final Integer osceletonUserId, final Joint joint, final UserState userState, final float[] coordinates) {
        final User user = this.mock(User.class);
        final UserFactory userFactory = this.mock(UserFactory.class);
        if (joint != null || userState != null) {
            this.checking(new Expectations() {

                {
                    oneOf(userFactory).create(with(osceletonUserId.intValue()));
                    will(returnValue(user));
                }
            });
        }
        final FactoryFacade factory = this.newFactoryFacade(user, joint, userState, coordinates);
        return new OscMessageTransformerImpl(factory);
    }

    private FactoryFacade newFactoryFacade(final User user, final Joint joint, final UserState userState, final float[] coordinates) {
        final Coordinate coordinate = this.mock(Coordinate.class);
        final JointMessage jointMessage = this.mock(JointMessage.class);
        final UserMessage userMessage = this.mock(UserMessage.class);
        final FactoryFacade factory = this.mock(FactoryFacade.class);
        if (joint != null || userState != null) {
            this.checking(new Expectations() {

                {
                    if (joint != null) {
                        oneOf(factory).createCoordinate(coordinates[0], coordinates[1], coordinates[2]);
                        will(returnValue(coordinate));
                    }
                    if (joint != null) {
                        oneOf(factory).createJointMessage(user, joint, coordinate);
                        will(returnValue(jointMessage));
                    } else if (userState != null) {
                        oneOf(factory).createUserMessage(user, userState);
                        will(returnValue(userMessage));
                    }
                }
            });
        }
        return factory;
    }
}
