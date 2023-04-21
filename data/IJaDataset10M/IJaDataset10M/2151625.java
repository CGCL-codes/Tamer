package br.ufpa.spider.mplan.logic.project;

import br.ufpa.spider.mplan.model.ApprovalProject;
import br.ufpa.spider.mplan.model.CollectionProcedureProject;
import br.ufpa.spider.mplan.model.CollectionVersionProject;
import br.ufpa.spider.mplan.model.MeasureProject;
import br.ufpa.spider.mplan.model.Organization;
import br.ufpa.spider.mplan.model.Project;
import br.ufpa.spider.mplan.model.User;
import br.ufpa.spider.mplan.persistence.CollectionProcedureProjectDAO;
import br.ufpa.spider.mplan.persistence.CollectionVersionProjectDAO;
import br.ufpa.spider.mplan.persistence.MeasureProjectDAO;
import br.ufpa.spider.mplan.util.Data;
import br.ufpa.spider.mplan.util.FormulaUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.script.ScriptException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Kaio Valente
 */
public class DefinitionCollectionDerivProjectController extends Window implements AfterCompose {

    private Window window;

    private Button importBtn;

    private Combobox measureCombobox;

    private Textbox typeCollection;

    private Textbox version;

    private Textbox note;

    private Textbox composition;

    private Textbox dateText;

    private Textbox formula;

    private Textbox value;

    private Textbox extract;

    private Grid listFormula;

    private Listbox listboxCollection;

    private Div divCollections;

    private Div leftContentDivBlue;

    private Div contentDivBlue;

    private Div rightContentDivBlue;

    private Div divTotal;

    private Label lbTotal;

    private Caption lbMnemonic;

    private User currentUser;

    public Session session;

    private Boolean update = false;

    private Organization userOrganization;

    private ApprovalProject approvalCollection;

    private Div mainDiv;

    private static final String TITLE = "Spider Mplan";

    private Project userProject;

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        session = window.getDesktop().getSession();
        currentUser = (User) session.getAttribute("user");
        update = (Boolean) session.getAttribute("update");
        userOrganization = (Organization) session.getAttribute("userOrganization");
        userProject = (Project) session.getAttribute("userProject");
        onLoad();
    }

    public void onLoad() {
        approvalCollection = (ApprovalProject) session.getAttribute("approvalProjectCollection");
        MeasureProject measure = approvalCollection.getMeasure();
        Comboitem item = measureCombobox.appendItem(measure.getId() + "-" + measure.getName());
        measureCombobox.setSelectedItem(item);
        version.setText(approvalCollection.getVersion());
        if (measure.getComposition() == 1) {
            composition.setText("Básica");
        } else {
            composition.setText("Derivada");
        }
        dateText.setText("" + Data.getDateTime());
        formula.setText(measure.getFormula().substring(0, measure.getFormula().length() - 1));
        List<String> listMnemonics = FormulaUtil.divideFormula(measure.getFormula());
        listFormula.setVisible(true);
        populateGrid(listMnemonics);
        if (listMnemonics.size() > 4) {
            increaseSpecialDiv(listMnemonics.size());
        }
        CollectionProcedureProject cp = CollectionProcedureProjectDAO.getCollectionProcedureByMeasure(userProject.getId(), measure.getId());
        if (cp.getType() == 1) {
            typeCollection.setText("Planilha Eletrônica");
        } else {
            typeCollection.setText("Manual");
        }
        session.removeAttribute("approvalProjectCollection");
    }

    public void increaseSpecialDiv(Integer size) {
        String height = contentDivBlue.getHeight().substring(0, contentDivBlue.getHeight().length() - 2);
        String mHeight = mainDiv.getHeight().substring(0, mainDiv.getHeight().length() - 2);
        Integer h = Integer.parseInt(height);
        Integer mh = Integer.parseInt(mHeight);
        h = (h + (size * 35));
        mh = (mh + (size * 35));
        contentDivBlue.setHeight(h + "px");
        leftContentDivBlue.setHeight(h + 2 + "px");
        rightContentDivBlue.setHeight(h + 2 + "px");
        mainDiv.setHeight("2000px");
    }

    public void populateGrid(List<String> list) {
        Rows rows = new Rows();
        rows.setParent(listFormula);
        Row row;
        for (String obj : list) {
            row = new Row();
            row.setParent(rows);
            Label label = new Label(obj);
            label.setParent(row);
            Button button = new Button("Carregar coletas de " + obj);
            button.setParent(row);
            button.setId(obj);
            button.addEventListener("onClick", new ClickButtonListener());
            label = new Label("");
            label.setParent(row);
            label.setId("id" + obj);
        }
    }

    public static void main(String[] args) throws ScriptException {
        FormulaUtil.getFormulaValue("testeinsert+testeinsertmedida&", "25.0:30.0");
    }

    public class ClickButtonListener implements EventListener {

        public void onEvent(Event event) throws UiException {
            try {
                String id = (String) event.getTarget().getId();
                MeasureProject measure = MeasureProjectDAO.getAllMeasureByMnemonic(id);
                List<CollectionVersionProject> listCollection = CollectionVersionProjectDAO.getCollectionVersionByMeasure(userOrganization.getId(), measure.getId(), userProject.getId());
                listboxCollection.getItems().clear();
                Integer h = 550;
                if (listCollection.size() == 0) {
                    String height = contentDivBlue.getHeight().substring(0, contentDivBlue.getHeight().length() - 2);
                    h = Integer.parseInt(height);
                    h = h - 300;
                    Messagebox.show("Não existem coletas para esta medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
                    divCollections.setVisible(false);
                    leftContentDivBlue.setHeight(h + 2 + "px");
                    contentDivBlue.setHeight(h + "px");
                    rightContentDivBlue.setHeight(h + 2 + "px");
                    mainDiv.setHeight(h + 50 + "px");
                    return;
                }
                Listitem listitem;
                Listcell listcell;
                for (CollectionVersionProject cv : listCollection) {
                    Data data = new Data();
                    listitem = new Listitem();
                    listitem.setParent(listboxCollection);
                    listitem.setValue("" + cv.getValue());
                    listitem.addEventListener("onDoubleClick", new onClickbtnSelect());
                    listcell = new Listcell(data.parseDataBra("" + cv.getDateCollection()));
                    listcell.setParent(listitem);
                    listcell = new Listcell(cv.getOwner());
                    listcell.setParent(listitem);
                    listcell = new Listcell("" + cv.getValue());
                    listcell.setParent(listitem);
                    listcell = new Listcell(cv.getNote());
                    listcell.setParent(listitem);
                }
                if (listboxCollection.getItemCount() != 0) {
                    listboxCollection.setSelectedIndex(0);
                }
                divCollections.setVisible(true);
                h = h + 300;
                leftContentDivBlue.setHeight(h + 2 + "px");
                contentDivBlue.setHeight(h + "px");
                rightContentDivBlue.setHeight(h + 2 + "px");
                mainDiv.setHeight(h + 50 + "px");
                lbMnemonic.setLabel(measure.getMenemonic());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class onClickbtnSelect implements EventListener {

        public void onEvent(Event event) throws UiException {
            try {
                onClick$btnSelect(event);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onClick$btnSelect(Event event) throws InterruptedException {
        String value = (String) listboxCollection.getSelectedItem().getValue();
        Label label = (Label) window.getFellow("id" + lbMnemonic.getLabel());
        label.setValue(value);
        if (FormulaUtil.validateFormula(listFormula)) {
            List listOfMnemonics = FormulaUtil.divideFormula(formula.getText() + "&");
            String values = FormulaUtil.getValuesFormula(listFormula);
            Double vlrFinal = (Double) FormulaUtil.totalFormula(formula.getText() + "&", listOfMnemonics, values);
            divTotal.setVisible(true);
            lbTotal.setValue("" + vlrFinal);
        }
    }

    public void onClick$save(Event event) throws InterruptedException {
        insert();
    }

    public void onClick$history(Event event) throws InterruptedException {
        List<CollectionVersionProject> listCollection = CollectionVersionProjectDAO.getCollectionVersionByMeasure(userOrganization.getId(), approvalCollection.getMeasure().getId(), userProject.getId());
        session.setAttribute("listCollectionProject", listCollection);
        Window wdw = (Window) Executions.createComponents("collectionProject/collectionHistoryProject.zul", null, null);
        wdw.setMaximizable(true);
        wdw.doModal();
        session.removeAttribute("listCollectionProject");
    }

    private void insert() throws UiException, InterruptedException {
        if (FormulaUtil.validateFormula(listFormula) == false) {
            Messagebox.show("Existem medidas sem valores selecionados", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        try {
            Date date = Data.formataData(dateText.getText());
            CollectionVersionProject cv = new CollectionVersionProject();
            cv.setOwner(currentUser.getName());
            cv.setNote(note.getText());
            cv.setDateCollection(date);
            cv.setMeasureProject(approvalCollection.getMeasure());
            cv.setValue(Double.parseDouble(lbTotal.getValue()));
            cv.setProject(userProject);
            CollectionVersionProjectDAO.addCollectionVersion(cv);
        } catch (EntityExistsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Messagebox.show("Um erro ocorreu ao acessar o banco de dados", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return;
        }
        try {
            Messagebox.show("Coleta armazenada com sucesso.", "Spider-MPlan", Messagebox.OK, Messagebox.INFORMATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onClick$cancel();
    }

    public void onClick$cancel() {
        Component parent = window.getParent();
        window.detach();
        Window wdw = (Window) Executions.createComponents("collectionProject/collectionProject.zul", parent, null);
        mainDiv.setHeight("350px");
        window = wdw;
    }
}
