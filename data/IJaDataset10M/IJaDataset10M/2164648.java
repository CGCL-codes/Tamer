package control.report;

import java.util.Iterator;
import java.util.List;
import javax.faces.event.ActionEvent;
import control.ViewControl;
import control.report.sistoc.OrcamentoPaginaInicial;
import control.report.sistoc.OrcamentoSeisCurvas;
import control.report.sistoc.OrcamentoTresCurvas;
import control.report.sistoc.ReportUpdateStrategy;
import model.business.Item;
import model.business.Ordem;
import model.quality.TestManager;
import java.util.Collection;

public abstract class ReportHandler {

    private Ordem ordem;

    private Integer numeroFolhas;

    private String txtDe = "1";

    private String txtAte = "1";

    private String option = "0";

    private ViewControl vc;

    private Boolean file;

    private Integer[] pagina = { 10, 18 };

    private Integer inicioItens = 12;

    private Integer fimItens = 16;

    public ReportHandler() {
        handler = new JakartaExcelHandler();
        TestManager tm = TestManager.getInstance();
        vc = tm.getViewControl();
        ordem = vc.getOrdem();
    }

    public void fileOutput(ActionEvent event) {
        this.file = true;
        this.calculoFolhas();
    }

    public void printerOutput(ActionEvent event) {
        this.file = false;
        this.calculoFolhas();
    }

    public void print(ActionEvent event) {
        this.updateFile();
        if (!this.file) this.getHandler().Print_doc();
    }

    public abstract void calculoFolhas();

    public abstract void updateFile();

    public Integer getNumeroFolhas() {
        return numeroFolhas;
    }

    public void setNumeroFolhas(Integer numeroFolhas) {
        this.numeroFolhas = numeroFolhas;
        this.setTxtAte(numeroFolhas.toString());
    }

    public String getTxtDe() {
        return txtDe;
    }

    public void setTxtDe(String txtDe) {
        this.txtDe = txtDe;
    }

    public String getTxtAte() {
        return txtAte;
    }

    public void setTxtAte(String txtAte) {
        this.txtAte = txtAte;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Ordem getOrdem() {
        return ordem;
    }

    public void setOrdem(Ordem ordem) {
        this.ordem = ordem;
    }

    public ViewControl getVc() {
        return vc;
    }

    public void setVc(ViewControl vc) {
        this.vc = vc;
    }

    /**
	 * @uml.property  name="reportUpdateStrategy"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="reportUpdater:control.report.sistoc.ReportUpdateStrategy"
	 * @uml.association  name="contem"
	 */
    private List reportUpdater;

    /**
	 * Getter of the property <tt>reportUpdateStrategy</tt>
	 * @return  Returns the reportUpdateStrategy.
	 * @uml.property  name="reportUpdateStrategy"
	 */
    public List getReportUpdater() {
        return reportUpdater;
    }

    public void setReportUpdater(List reportUpdater) {
        this.reportUpdater = reportUpdater;
    }

    /**
	 * @uml.property  name="excelHandler"
	 * @uml.associationEnd  inverse="reportHandler:control.report.ExcelHandler"
	 * @uml.association  name="controla"
	 */
    private ExcelHandler handler;

    /**
	 * Getter of the property <tt>excelHandler</tt>
	 * @return  Returns the excelHandler.
	 * @uml.property  name="excelHandler"
	 */
    public ExcelHandler getHandler() {
        return handler;
    }

    /**
	 * Setter of the property <tt>excelHandler</tt>
	 * @param excelHandler  The excelHandler to set.
	 * @uml.property  name="excelHandler"
	 */
    public void setHandler(ExcelHandler handler) {
        this.handler = handler;
    }
}
