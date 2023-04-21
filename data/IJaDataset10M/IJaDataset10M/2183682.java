package org.jabusuite.webclient.article;

import javax.naming.NamingException;
import nextapp.echo2.app.ContentPane;
import org.jabusuite.webclient.controls.JbsExtent;
import org.jabusuite.webclient.controls.container.JbsGrid;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import org.jabusuite.article.ArticleVAT;
import org.jabusuite.article.session.ArticleVATsRemote;
import org.jabusuite.webclient.main.ClientGlobals;
import org.jabusuite.client.utils.ClientTools;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.webclient.controls.JbsCheckbox;
import org.jabusuite.webclient.controls.JbsTextField;
import org.jabusuite.webclient.dataediting.DlgState;
import org.jabusuite.webclient.dataediting.PnEditJbsObject;
import org.jabusuite.webclient.main.JbsL10N;
import org.jabusuite.logging.Logger;

/**
 * @author hilwers
 * @date 02.07.2007
 *
 */
public class PnVATEdit extends PnEditJbsObject {

    private static final long serialVersionUID = 6999374816962206032L;

    private Logger logger = Logger.getLogger(PnVATEdit.class);

    protected JbsTextField txName;

    protected JbsTextField txValue;

    protected JbsCheckbox cbStdStandard;

    protected JbsCheckbox cbStdReduced;

    public PnVATEdit() {
        super(DlgState.dsInsert);
        this.setVAT(new ArticleVAT());
    }

    public PnVATEdit(DlgState state, ArticleVAT vat) {
        super(state);
        this.setVAT(vat);
    }

    @Override
    protected void createComponents() {
        this.txName = new JbsTextField();
        this.txValue = new JbsTextField();
        this.cbStdReduced = new JbsCheckbox();
        this.cbStdStandard = new JbsCheckbox();
    }

    @Override
    protected void getControlData() {
        if (this.getVAT() != null) {
            this.getVAT().setName(this.txName.getText());
            this.getVAT().setValue(Float.valueOf(this.txValue.getText()));
            this.getVAT().setStdReduced(this.cbStdReduced.isSelected());
            this.getVAT().setStdStandard(this.cbStdStandard.isSelected());
        }
    }

    @Override
    protected void initPanel() {
        ContentPane cpStdData = new ContentPane();
        JbsGrid grdMain = new JbsGrid(2);
        grdMain.setInsets(new Insets(5, 5));
        grdMain.setColumnWidth(0, new JbsExtent(300));
        grdMain.add(new Label(JbsL10N.getString("ArticleVAT.name")));
        grdMain.add(txName);
        grdMain.add(new Label(JbsL10N.getString("ArticleVAT.value")));
        grdMain.add(txValue);
        grdMain.add(new Label(JbsL10N.getString("ArticleVAT.stdStandard")));
        grdMain.add(cbStdStandard);
        grdMain.add(new Label(JbsL10N.getString("ArticleVAT.stdReduced")));
        grdMain.add(cbStdReduced);
        cpStdData.add(grdMain);
        this.add(cpStdData);
    }

    @Override
    protected void setControlData() {
        if (this.getVAT() != null) {
            this.txName.setText(this.getVAT().getName());
            this.txValue.setText(String.valueOf(this.getVAT().getValue()));
            this.cbStdReduced.setSelected(this.getVAT().isStdReduced());
            this.cbStdStandard.setSelected(this.getVAT().isStdStandard());
        }
    }

    @Override
    public void doSave() throws EJbsObject {
        logger.debug("Saving data...");
        try {
            ArticleVATsRemote vats = (ArticleVATsRemote) ClientTools.getRemoteBean(ArticleVATsRemote.class);
            super.doSave();
            if (this.getDlgState() == DlgState.dsInsert) {
                logger.debug("Adding new entity " + this.getVAT().getId() + ".");
                vats.createDataset(this.getVAT(), ClientGlobals.getUser(), ClientGlobals.getCompany());
            } else if (this.getDlgState() == DlgState.dsEdit) {
                logger.debug("Saving existing entity " + this.getVAT().getId() + ".");
                vats.updateDataset(this.getVAT(), ClientGlobals.getUser());
            }
            logger.debug("Entity saved.");
        } catch (NamingException e) {
            logger.error("Error svaing vat.", e);
        }
    }

    protected void setVAT(ArticleVAT vat) {
        this.setJbsBaseObject(vat);
    }

    protected ArticleVAT getVAT() {
        return (ArticleVAT) this.getJbsBaseObject();
    }
}
