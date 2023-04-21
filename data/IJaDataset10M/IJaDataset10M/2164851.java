package org.actorsguildframework.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import org.actorsguildframework.Actor;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.ConfigurationException;
import org.actorsguildframework.annotations.ConcurrencyModel;
import org.actorsguildframework.annotations.Initializer;
import org.actorsguildframework.annotations.Message;
import org.actorsguildframework.annotations.Model;
import org.actorsguildframework.annotations.Shared;
import org.actorsguildframework.annotations.ThreadSafe;
import org.actorsguildframework.immutable.ImmutableHelper;

/**
 * Describes the configuration of an Actor sub-class.
 * Immutable thread-safe.
 * @see MessageImplDescriptor
 */
public final class ActorClassDescriptor {

    /**
	 * The default concurrency model of the actor. 
	 */
    private final ConcurrencyModel concurrencyModel;

    /**
	 * The message implementations of the actor class.
	 */
    private final MessageImplDescriptor[] messages;

    /**
	 * The {@link BeanClassDescriptor} of the actor.
	 */
    private final BeanClassDescriptor beanClassDescriptor;

    /**
	 * Creates a new instance.
	 * @param concurrencyModel the concurrency model used by the actor
	 * @param msgDescriptors the message descriptors of the actor
	 */
    private ActorClassDescriptor(ConcurrencyModel concurrencyModel, MessageImplDescriptor[] msgDescriptors, BeanClassDescriptor beanClassDescriptor) {
        this.concurrencyModel = concurrencyModel;
        this.messages = new MessageImplDescriptor[msgDescriptors.length];
        System.arraycopy(msgDescriptors, 0, this.messages, 0, msgDescriptors.length);
        this.beanClassDescriptor = beanClassDescriptor;
    }

    /**
	 * Creates a new ActorClassDescriptor for the given actor class.
	 * @param actorClass the actor's class
	 * @return the new instance
	 * @throws ConfigurationException if the agent is not configured correctly
	 */
    public static ActorClassDescriptor create(Class<? extends Actor> actorClass) {
        if (Modifier.isInterface(actorClass.getModifiers())) throw new ConfigurationException(String.format("Actor class %s is an interface. You may implement an interface in an actor, but you can not instantiate it.", actorClass));
        if (Modifier.isFinal(actorClass.getModifiers())) throw new ConfigurationException(String.format("Actor class %s is final. Actors can not be final, because the Agent needs to create a sub-class of the Actor.", actorClass));
        if ((actorClass.getDeclaringClass() != null) && !Modifier.isStatic(actorClass.getModifiers())) throw new ConfigurationException(String.format("Actor class %s is a non-static inner class. If you declare an actor as inner class, you must use the 'static' modifier.", actorClass));
        Constructor<?> ctor;
        try {
            ctor = actorClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException(String.format("Actor class %s does not have a default constructor (without arguments). " + "A default constructor is required for every actor. If you need to initialize an Actor with arguments, use properties and override the init() method.", actorClass), e);
        }
        if (Modifier.isPrivate(ctor.getModifiers())) {
            if (Modifier.isPrivate(actorClass.getModifiers())) throw new ConfigurationException(String.format("Constructor of class %s must not be private. It is allowed to declare a private actor, but then you must define a non-private constructor without argument. Every actor needs a non-private default constructor.", actorClass)); else throw new ConfigurationException(String.format("Constructor of class %s must not be private. Every actor needs a non-private default constructor.", actorClass));
        }
        Model ac = actorClass.getAnnotation(Model.class);
        ConcurrencyModel actorConcurrencyModel = (ac != null) ? ac.value() : ConcurrencyModel.SingleThreaded;
        ArrayList<Method> messages = new ArrayList<Method>();
        for (Method method : BeanClassDescriptor.getAllMethods(actorClass, Actor.class)) {
            int mods = method.getModifiers();
            if (method.getAnnotation(Message.class) != null) messages.add(method); else if (method.getReturnType().equals(AsyncResult.class) && ((mods & (Modifier.PRIVATE | Modifier.FINAL | Modifier.STATIC)) == 0)) throw new ConfigurationException(String.format("In Actor class %s, method %s returns AsyncResult, " + "but has no @Message annotation. \n" + "This is not allowed, in order to prevent methods that are accidentally not declared as messages. " + "Please note that you also need to specify @Message if you are overriding " + "an inherited method (Java annotations support the concept of inheritance only for classes, not " + "for methods). If you are really sure that your method should not be a message, you " + "must declare it either as 'private' or 'final'.", actorClass.getName(), method.getName())); else if (Modifier.isPublic(mods) && (method.getAnnotation(ThreadSafe.class) == null) && (method.getAnnotation(Initializer.class) == null) && !Modifier.isAbstract(mods) && !Modifier.isStatic(mods)) throw new ConfigurationException(String.format("Method %s in actor %s is public," + "but neither a @Message, @Initializer, @Prop nor declared @ThreadSafe.\n" + "This is not allowed. If you are sure that this method is thread-safe, " + "add a @ThreadSafe. If you don't call this method from other classes, make " + "it private or protected.", method.getName(), actorClass.getName()));
        }
        MessageImplDescriptor[] msgDescriptors = new MessageImplDescriptor[messages.size()];
        for (int i = 0; i < msgDescriptors.length; i++) msgDescriptors[i] = MessageImplDescriptor.createMessageDescriptor(actorClass, actorConcurrencyModel, messages.get(i));
        BeanClassDescriptor bcd = BeanClassDescriptor.create(actorClass);
        if (actorConcurrencyModel == ConcurrencyModel.Stateless) checkStatelessActor(actorClass, bcd);
        ActorClassDescriptor instance = new ActorClassDescriptor(actorConcurrencyModel, msgDescriptors, bcd);
        return instance;
    }

    /**
	 * Checks that the given actor does not contain any state.
	 * @param actorClass the actor to check
	 * @param bcd the BeanClassDescriptor of the actor
	 * @throws ConfigurationException if the actor contains state
	 */
    private static void checkStatelessActor(Class<? extends Actor> actorClass, BeanClassDescriptor bcd) {
        for (int i = 0; i < bcd.getPropertyCount(); i++) {
            PropertyDescriptor pd = bcd.getProperty(i);
            if (pd.getPropertySource().isGenerating()) {
                if (pd.getSetter() != null) throw new ConfigurationException(String.format("Property %s in actor %s (getter: %s) is not a read-only " + "property, even though the actor is declared as stateless (@Model annotation). A stateless actor " + "can only have read-only properties.", pd.getName(), actorClass.getName(), pd.getGetter().getName()));
                if (!(pd.isSharedReference() || isStatelessCompatible(pd.getPropertyClass()))) throw new ConfigurationException(String.format("Property %s in actor %s (getter: %s) has the non-stateless-compatible " + "type %s without @Shared annotation, even though the actor is declared as stateless (@Model annotation). " + "A stateless actor can only store immutable types, other actors and multi-threading-safe classes " + "with @Shared annotation.", pd.getName(), actorClass.getName(), pd.getGetter().getName(), pd.getPropertyClass().getName()));
            }
        }
        checkStatelessFields(actorClass, actorClass);
    }

    /**
	 * Checks that the given actor does not contain any state in its fields, recursively going to super-classes.
	 * @param topActorClass the actor class that which contains actorClass and it the actual class being checked (for error messages)
	 * @param actorClass the actor class to check now
	 */
    private static void checkStatelessFields(Class<? extends Actor> topActorClass, Class<?> actorClass) {
        for (Field f : actorClass.getDeclaredFields()) {
            if (!Modifier.isFinal(f.getModifiers())) throw new ConfigurationException(String.format("Field %s in %s is not final, even though the actor %s is " + "declared as stateless (@Model annotation). A stateless actor can only have final fields of " + "immutable types or thread-safe types.", f.getName(), actorClass.getName(), topActorClass.getName()));
            if (!((f.getAnnotation(Shared.class) != null) || isStatelessCompatible(f.getType()))) throw new ConfigurationException(String.format("Field %s in %s has the non-stateless-compatible " + "type %s without @Shared annotation, even though the actor %s is declared as stateless (@Model annotation). " + "A stateless actor can only store immutable types, other actors and multi-threading-safe classes " + "with @Shared annotation.", f.getName(), actorClass.getName(), f.getType().getName(), topActorClass.getName()));
        }
        if (!actorClass.getSuperclass().equals(Actor.class)) checkStatelessFields(topActorClass, actorClass.getSuperclass());
    }

    /**
	 * Returns the effective concurrency model of the Actor. 
	 * @return the ConcurrencyModel
	 */
    public ConcurrencyModel getConcurrencyModel() {
        return concurrencyModel;
    }

    /**
	 * Returns the message at the given index.
	 * @param index the index of the message
	 * @return the descriptor
	 * @throws IndexOutOfBoundsException if there is no message at the given index 
	 * @see #getMessageCount()
	 */
    public MessageImplDescriptor getMessage(int index) {
        return messages[index];
    }

    /**
	 * Returns the number of messages.
	 * @return the number of messages
	 */
    public int getMessageCount() {
        return messages.length;
    }

    /**
	 * Returns the actor's BeanClassDescriptor.
	 * @return the BeanClassDescriptor
	 */
    public BeanClassDescriptor getBeanClassDescriptor() {
        return beanClassDescriptor;
    }

    /**
	 * Checks whether the given class is compatible with {@link ConcurrencyModel#Stateless} actors or messages, following
	 * the type restrictions for them.
	 * @param c the class to check
	 * @return true if compatible, false otherwise
	 */
    private static boolean isStatelessCompatible(Class<?> c) {
        assert c != null;
        if (ImmutableHelper.isImmutableType(c)) return true; else if (Actor.class.isAssignableFrom(c)) return true; else return false;
    }
}
