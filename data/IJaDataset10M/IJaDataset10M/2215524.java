package org.scribble.conversation.model;

import org.scribble.extensions.RegistryFactory;
import org.scribble.model.*;
import org.scribble.model.admin.DefaultModelListener;
import org.scribble.model.admin.ModelInfo;
import org.scribble.model.admin.ModelRepository;

/**
 * This class represents the composition semantics associated
 * with the Run and Spawn constructs.
 * 
 */
public abstract class Compose extends ModelInclude {

    private static final long serialVersionUID = -197685339387762151L;

    /**
	 * This is the default constructor.
	 * 
	 */
    public Compose() {
    }

    /**
	 * This method returns the associated conversation.
	 * 
	 * @return The conversation
	 */
    public Conversation getDefinition() {
        Conversation ret = m_inlineDefinition;
        if (ret == null && m_reference != null) {
            if (m_reference.isInner()) {
                ModelObject cur = getParent();
                while (ret == null && cur != null) {
                    if (cur instanceof Conversation) {
                        Conversation conv = (Conversation) cur;
                        for (int i = 0; ret == null && i < conv.getBlock().getContents().size(); i++) {
                            if (conv.getBlock().getContents().get(i) instanceof Conversation) {
                                Conversation subconv = (Conversation) conv.getBlock().getContents().get(i);
                                if (subconv.getLocatedName() != null && subconv.getLocatedName().getName() != null && m_reference.getAlias().equals(subconv.getLocatedName().getName()) && ((m_reference.getLocatedRole() == null && subconv.getLocatedName().getRole() == null) || ((m_reference.getLocatedRole() != null && subconv.getLocatedName().getRole() != null && m_reference.getLocatedRole().equals(subconv.getLocatedName().getRole().getName()))))) {
                                    ret = subconv;
                                }
                            }
                        }
                    }
                    cur = cur.getParent();
                }
            } else {
                ModelRepository mrep = (ModelRepository) RegistryFactory.getRegistry().getExtension(ModelRepository.class, null);
                if (mrep != null) {
                    java.util.List<ModelInfo> models = mrep.getModels(m_reference, new DefaultModelListener());
                    for (int i = 0; ret == null && i < models.size(); i++) {
                        if (models.get(i).getModel() instanceof ConversationModel) {
                            ret = ((ConversationModel) models.get(i).getModel()).getConversation();
                        }
                    }
                }
            }
        }
        return (ret);
    }

    /**
	 * This method returns the conversation reference associated
	 * with the run construct.
	 * 
	 * @return The conversation reference, or null if not defined
	 */
    @Reference(containment = true)
    public ConversationReference getReference() {
        return (m_reference);
    }

    /**
	 * This method sets the conversation reference associated
	 * with the run construct.
	 * 
	 * @param ref The conversation reference
	 */
    public void setReference(ConversationReference ref) {
        if (m_reference != null) {
            m_reference.setParent(null);
        }
        m_reference = ref;
        if (m_reference != null) {
            m_reference.setParent(this);
        }
    }

    /**
	 * This method indicates whether the model include is
	 * an inline definition.
	 * 
	 * @return Whether an inline definition
	 */
    public boolean isInline() {
        return (getInlineDefinition() != null);
    }

    /**
	 * This method returns the inline definition associated
	 * with the model include construct.
	 * 
	 * @return The inline definition, or null if not defined
	 */
    @Reference(containment = true)
    public Conversation getInlineDefinition() {
        return (m_inlineDefinition);
    }

    /**
	 * This method sets the inline definition associated
	 * with the run construct.
	 * 
	 * @param definition The inner definition
	 */
    public void setInlineDefinition(Conversation definition) {
        if (m_inlineDefinition != null) {
            m_inlineDefinition.setParent(null);
        }
        m_inlineDefinition = definition;
        if (m_inlineDefinition != null) {
            m_inlineDefinition.setParent(this);
        }
    }

    private ConversationReference m_reference = null;

    private Conversation m_inlineDefinition = null;
}
