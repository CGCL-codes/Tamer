package org.ras.bin.checklister.tax.genus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.jz.xs.binding.DataLink;
import org.jz.xs.entity.EntityInstance;
import org.jz.xs.frames.JdbcCardListFrame;
import org.jz.xs.frames.JdbcInstanceCard;
import org.jz.xs.frames.JdbcKernel;
import org.jz.xs.frames.JdbcListFrame;
import org.jz.xs.jdbc.JdbcDecryptableDataLink;
import org.ras.bin.checklister.tax.family.FamilyCard;
import org.ras.bin.checklister.tax.family.FamilyCardListFrame;

/**
 *
 * @author jz
 */
public class CardDataLink extends DataLink {

    private String fFieldName;

    private EntityInstance fInstance;

    private JdbcInstanceCard fCard;

    private Class<org.jz.xs.frames.JdbcInternalFrame> fListFormClass;

    private JdbcKernel fKernel;

    public CardDataLink(JdbcKernel _Kernel, JdbcInstanceCard _Card, EntityInstance _Instance, String _FieldName, Class _ListFormClass) throws Exception {
        super(_Card, _Instance.getValueByName(_FieldName));
        fKernel = _Kernel;
        fCard = _Card;
        fInstance = _Instance;
        fFieldName = _FieldName;
        fListFormClass = _ListFormClass;
        fCard.setKernel(fKernel);
    }

    @Override
    protected void registerOnView() {
        fCard.setChangeActionListener(new ChangeActionListener());
    }

    @Override
    protected void unregisterOnView() {
        fCard.setChangeActionListener(null);
    }

    @Override
    protected void updateView() {
        try {
            fCard.loadInstance(fInstance.getValueByName(fFieldName).getValue());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(fCard, e.getMessage(), "Instace Card Link update error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void bind() throws Exception {
        registerOnView();
        fInstance.getValueByName(fFieldName).addChangeListener(this);
        fCard.loadInstance(fInstance.getValueByName(fFieldName).getValue());
        fCard.switchToChangeableMode();
    }

    @Override
    public void unbind() throws Exception {
        fInstance.getValueByName(fFieldName).removeChangeListener(this);
    }

    protected class ChangeActionListener extends AbstractAction {

        public void actionPerformed(ActionEvent evt) {
            try {
                Object uncastedFrm = fListFormClass.newInstance();
                if (uncastedFrm instanceof JdbcListFrame) {
                    JdbcListFrame frm = (JdbcListFrame) uncastedFrm;
                    fCard.getKernel().getDesktopPane().add(frm);
                    frm.selectInstance(fCard.getKernel(), new EndChangeActionListener());
                } else if (uncastedFrm instanceof JdbcCardListFrame) {
                    JdbcCardListFrame frm = (JdbcCardListFrame) uncastedFrm;
                    fCard.getKernel().getDesktopPane().add(frm);
                    frm.selectInstance(fCard.getKernel(), new EndChangeActionListener());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(fView, "Cannot initialize selection process : " + ex.getMessage(), "DataLink", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected class EndChangeActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() instanceof EntityInstance) {
                    fInstance.getValueByName(fFieldName).setValue(((EntityInstance) (e.getSource())).getValueByName(fFieldName).getValue());
                    updateView();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(fView, "Cannot finalize selection process : " + ex.getMessage(), "DataLink", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
