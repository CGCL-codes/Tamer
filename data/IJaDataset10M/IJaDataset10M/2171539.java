package org.modelibra.swing.domain.model.concept.entity.neighbor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.modelibra.Entities;
import org.modelibra.IEntities;
import org.modelibra.IEntity;
import org.modelibra.action.EntitiesAction;
import org.modelibra.action.UpdateAction;
import org.modelibra.config.NeighborConfig;
import org.modelibra.exception.ModelibraRuntimeException;
import org.modelibra.swing.widget.ModelibraFrame;
import org.modelibra.swing.widget.ModelibraPanel;
import org.modelibra.util.NatLang;

@SuppressWarnings("serial")
public class EntityParentLookupPanel extends ModelibraPanel {

    private JLabel messageLabel;

    private ParentLookupBridge parentLookupBridge;

    public EntityParentLookupPanel(ModelibraFrame contentFrame, boolean add, final IEntities<?> entities, final IEntity<?> entity, NeighborConfig parentNeighborConfig) {
        if (add) {
            parentLookupBridge = new ParentLookupBridge(entity, parentNeighborConfig);
        } else {
            parentLookupBridge = new ParentLookupBridge(entity.copy(), parentNeighborConfig);
        }
        if (parentNeighborConfig.isUpdate() && parentNeighborConfig.isParent()) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            addLookupLabels(contentFrame, parentNeighborConfig);
            addParentLookup(contentFrame, add, entities, entity);
        }
    }

    protected void addLookupLabels(ModelibraFrame contentFrame, NeighborConfig neighborConfig) {
        JPanel messagePanel = new JPanel();
        messagePanel.add(new JLabel(contentFrame.getApp().getNatLang().getText(neighborConfig.getConceptConfig().getCode() + "." + neighborConfig.getCode()) + ": "));
        messageLabel = new JLabel("");
        messagePanel.add(messageLabel);
        add(messagePanel);
    }

    protected void addParentLookup(final ModelibraFrame contentFrame, final boolean add, final IEntities<?> entities, final IEntity<?> entity) {
        IEntities<?> lookupEntities = parentLookupBridge.getLookupEntities();
        if (lookupEntities != null) {
            List<String> entityStringList = ((Entities<?>) lookupEntities).getEssentialPropertiesStringList();
            String[] entityArray = new String[lookupEntities.size()];
            final JComboBox comboBox = new JComboBox();
            if (parentLookupBridge.getParentNeighborConfig().isOptional()) {
                comboBox.setEditable(true);
            }
            add(comboBox);
            comboBox.setModel(new DefaultComboBoxModel(entityStringList.toArray(entityArray)));
            IEntity<?> parentEntity = parentLookupBridge.getParentEntity();
            if (parentEntity != null) {
                comboBox.setSelectedItem(parentLookupBridge.getParentEntityEssentialString());
            } else {
                if (parentLookupBridge.getParentNeighborConfig().isOptional()) {
                    comboBox.setSelectedItem(null);
                } else {
                    throw new ModelibraRuntimeException("Parent is mandatory.");
                }
            }
            comboBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String entityEssentialString = (String) comboBox.getSelectedItem();
                    IEntity<?> lookedupEntity = null;
                    if (entityEssentialString != null) {
                        lookedupEntity = parentLookupBridge.getLookupEntity(entityEssentialString);
                    } else if (parentLookupBridge.getParentNeighborConfig().isMandatory()) {
                        throw new ModelibraRuntimeException("Parent is mandatory.");
                    }
                    parentLookupBridge.setParentEntity(lookedupEntity);
                    clearMessage();
                    if (!add) {
                        EntitiesAction action = new UpdateAction(contentFrame.getApp().getModelSession(), entities, entity, parentLookupBridge.getEntity());
                        action.execute();
                        if (!action.isExecuted()) {
                            NatLang natLang = contentFrame.getApp().getNatLang();
                            setMessage(natLang.getText("updateNot") + " " + getErrorMsgsByKeys(entities, natLang));
                        }
                    }
                }
            });
        }
    }

    public String getParentNeighborName() {
        return parentLookupBridge.getParentNeighborName();
    }

    private void setMessage(String message) {
        messageLabel.setText(message);
    }

    private void clearMessage() {
        setMessage(" ");
    }
}
