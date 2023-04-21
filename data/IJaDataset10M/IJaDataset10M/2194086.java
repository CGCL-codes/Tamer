package org.likken.core.schema;

import org.likken.util.parser.*;
import org.likken.core.*;
import java.util.HashMap;

/**
 * @author Stephane Boisson <s.boisson@focal-net.com> 
 * @version $Revision: 1.1 $ $Date: 2000/12/07 22:49:37 $
 */
public abstract class SchemaObjectBuilder implements Grammar {

    private HashMap subStates;

    private BuilderState state;

    private BuilderState startState, oidState, propertiesState;

    private String errorMessage;

    public SchemaObjectBuilder() {
        subStates = new HashMap();
        addSubState(Constants.NAME, new BuilderListState() {

            public void visitQuotedDescriptorToken(final QuotedDescriptorToken aToken) {
                try {
                    if (isList()) {
                        getSchemaObject().addNameAlias(aToken.getDescriptor());
                    } else {
                        getSchemaObject().setName(aToken.getDescriptor());
                        commit();
                    }
                } catch (final DuplicateObjectException e) {
                    handleError(e);
                }
            }
        });
        addSubState(Constants.DESC, new BuilderState() {

            public void visitStringToken(final StringToken aToken) {
                getSchemaObject().setDescription(aToken.getString());
                commit();
            }

            public void visitQuotedDescriptorToken(final QuotedDescriptorToken aToken) {
                getSchemaObject().setDescription(aToken.getDescriptor());
                commit();
            }
        });
        addSubState(Constants.OBSOLETE, new BuilderState() {

            public void enter() {
                getSchemaObject().setObsolete(true);
                commit();
            }
        });
    }

    protected abstract class BuilderState extends TokenVisitor {

        public void enter() {
        }

        public void exit() {
        }

        public void visitDefault(final Token aToken) {
            handleError("Unexpected token: " + aToken);
        }

        protected final void commit() {
            setNextState(propertiesState);
        }

        protected final void handleError(final String aMessage) {
            if (getSchemaObject() != null) {
                errorMessage = aMessage + " (in definition of " + getSchemaObject() + ")";
            } else {
                errorMessage = aMessage;
            }
            setNextState(null);
        }

        protected final void handleError(final Throwable aThrowable) {
            errorMessage = aThrowable.toString();
            setNextState(null);
        }
    }

    protected abstract class BuilderListState extends BuilderState {

        private boolean isList;

        public final void setList(final boolean aFlag) {
            isList = aFlag;
        }

        public final boolean isList() {
            return isList;
        }

        public void enter() {
            setList(false);
        }

        public void visitStartToken(final StartToken aToken) {
            setList(true);
        }

        public void visitSeparatorToken(final SeparatorToken aToken) {
            if (!isList()) {
                handleError("Unexpected seperator");
            }
        }

        public void visitEndToken(final EndToken aToken) {
            if (isList()) {
                commit();
            } else {
                handleError("Unexpected end of list");
            }
        }
    }

    protected final void addSubState(final String aDescriptor, final BuilderState aState) {
        subStates.put(aDescriptor, aState);
    }

    public void begin() throws GrammarException {
        startState = new BuilderState() {

            public void visitStartToken(final StartToken aToken) {
                setNextState(oidState);
            }
        };
        oidState = new BuilderState() {

            public void visitNumericOidToken(final NumericOidToken aToken) {
                try {
                    beginObjectCreation(aToken.getOid());
                    setNextState(propertiesState);
                } catch (final DirectoryObjectException e) {
                    handleError(e);
                }
            }
        };
        propertiesState = new BuilderState() {

            public void visitDescriptorToken(final DescriptorToken aToken) {
                String descriptor = aToken.getDescriptor().toUpperCase();
                BuilderState sub_state = (BuilderState) subStates.get(descriptor);
                if (sub_state != null) {
                    setNextState(sub_state);
                } else {
                    handleError("Unknown term '" + aToken.getDescriptor() + '\'');
                }
            }

            public void visitEndToken(final EndToken aToken) {
                try {
                    endObjectCreation();
                    setNextState(startState);
                } catch (final DirectoryObjectException e) {
                    handleError(e);
                }
            }
        };
        state = startState;
    }

    public void end() throws GrammarException {
    }

    public void pushToken(Token aToken) throws GrammarException {
        aToken.accept(state);
        if (state == null) {
            throw new GrammarException(errorMessage);
        }
    }

    protected final void setNextState(BuilderState aState) {
        state.exit();
        state = aState;
        if (state != null) {
            state.enter();
        }
    }

    protected abstract void beginObjectCreation(final String anOid) throws DirectoryObjectException;

    protected abstract void endObjectCreation() throws DirectoryObjectException;

    protected abstract SchemaObject getSchemaObject();
}
