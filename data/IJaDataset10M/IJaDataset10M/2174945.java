package report.jasper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import message.MessageId;
import message.MessageRepository;
import message.SimplePropertiesIconRepository;
import model.money.MoneyAmount;
import model.receipt.Sell;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import persistence.ModelPersistence;
import query.framework.results.LazySearchResults;
import query.framework.results.SearchResults;
import query.framework.results.SellItemsLazySearchResults;
import report.ReportFactory;
import report.ReportPrint;
import report.ReportPrintException;

public class JasperReportFactory extends ReportFactory {

    private static final Map<Class<?>, String> patternByClass = new HashMap<Class<?>, String>();

    static {
        patternByClass.put(MoneyAmount.class, MoneyAmount.pattern());
    }

    public ReportPrint sellReportPrint(Sell sell) {
        LazySearchResults results = new SellItemsLazySearchResults(sell.items());
        HashMap parameters = new HashMap();
        addLabel(parameters, MessageId.sell);
        addLabel(parameters, MessageId.article);
        addLabel(parameters, MessageId.count);
        addLabel(parameters, MessageId.unitPrice);
        addLabel(parameters, MessageId.client);
        addLabel(parameters, MessageId.clientDebt);
        addLabel(parameters, MessageId.paymentTotal);
        addLabel(parameters, MessageId.total);
        addParameter(parameters, MessageId.total, sell.sellTotal());
        addParameter(parameters, MessageId.paymentTotal, sell.paymentTotal());
        MoneyAmount debt = ModelPersistence.instance().loadedModel().store().debts().debtOf(sell.client());
        addParameter(parameters, MessageId.clientDebt, debt);
        addParameter(parameters, MessageId.date, sell.date());
        addParameter(parameters, MessageId.client, sell.client());
        return reportPrintFor(reportFor("Sell"), results, parameters);
    }

    private static void addParameter(Map parameters, MessageId messageId, Object object) {
        parameters.put(messageId.toString(), ResultsJRDataSourceAdapter.adaptedValueFor(object));
    }

    private static void addLabel(Map parameters, MessageId messageId) {
        parameters.put(messageId.toString() + "Label", MessageRepository.instance().get(messageId));
    }

    private static ReportPrint reportPrintFor(JasperReport report, SearchResults results, HashMap parameters) {
        JasperPrint print;
        try {
            JRDataSource dataSource = new ResultsJRDataSourceAdapter(results);
            print = JasperFillManager.fillReport(report, parameters, dataSource);
        } catch (Exception e) {
            throw new ReportPrintException(e);
        }
        return new JasperReportPrint(print);
    }

    private static JasperReport reportFor(String reportName) {
        try {
            return JasperCompileManager.compileReport(urlFor(reportName).openStream());
        } catch (Exception e) {
            throw new ReportPrintException(e);
        }
    }

    private static URL urlFor(String reportName) {
        return SimplePropertiesIconRepository.class.getResource("/jasper/" + reportName + ".jrxml");
    }

    public ReportPrint standardListReportPrint(SearchResults results, String title) {
        return reportPrintFor(standardListReport(results, title), results, new HashMap());
    }

    private JasperReport standardListReport(SearchResults results, String title) {
        try {
            JasperDesign design = (JasperDesign) JRXmlLoader.load(urlFor("StandardList").openStream());
            JRDesignBand headerBand = (JRDesignBand) design.getTitle();
            JRDesignBand detailBand = (JRDesignBand) design.getDetail();
            JRDesignElement templateHeader = (JRDesignElement) headerBand.getElementByKey("staticText-1");
            JRDesignElement templateField = (JRDesignElement) detailBand.getElementByKey("textField-1");
            JRDesignStaticText pageTitle = (JRDesignStaticText) headerBand.getElementByKey("staticText-2");
            pageTitle.setText(title);
            for (int i = 0; i < results.getColumnCount(); i++) {
                int cloneWidth = templateHeader.getWidth() / results.getColumnCount();
                int cloneX = i * cloneWidth;
                String columnName = results.getColumnName(i);
                String columnDescription = results.getColumnDescription(i);
                Class<?> columnClass = results.getColumnClass(i);
                design.addField(columnField(columnName));
                headerBand.addElement(columnHeaderElement(templateHeader, cloneWidth, i, cloneX, columnDescription));
                detailBand.addElement(columnValueElement(templateField, cloneWidth, i, cloneX, columnName, columnClass));
            }
            headerBand.removeElement(templateHeader);
            detailBand.removeElement(templateField);
            return JasperCompileManager.compileReport(design);
        } catch (Exception e) {
            throw new ReportPrintException(e);
        }
    }

    private JRDesignElement columnValueElement(JRDesignElement templateField, int cloneWidth, int i, int cloneX, String columnName, Class<?> columnClass) {
        JRDesignTextField cloneColumnValue = (JRDesignTextField) templateField.clone();
        cloneColumnValue.setX(cloneX);
        cloneColumnValue.setWidth(cloneWidth);
        cloneColumnValue.setKey("columnValue" + i);
        cloneColumnValue.setPattern(patternFor(columnClass));
        JRDesignExpression expression = new JRDesignExpression();
        expression.setValueClass(ResultsJRDataSourceAdapter.adaptedClassFor(columnClass));
        expression.setText("$F{" + columnName + "}");
        cloneColumnValue.setExpression(expression);
        return cloneColumnValue;
    }

    private String patternFor(Class<?> clazz) {
        return patternByClass.get(clazz);
    }

    private JRDesignElement columnHeaderElement(JRDesignElement templateHeader, int cloneWidth, int i, int cloneX, String columnDescription) {
        JRDesignStaticText cloneColumnHeader = (JRDesignStaticText) templateHeader.clone();
        cloneColumnHeader.setX(cloneX);
        cloneColumnHeader.setWidth(cloneWidth);
        cloneColumnHeader.setKey("columnHeader" + i);
        cloneColumnHeader.setText(columnDescription);
        return cloneColumnHeader;
    }

    private JRDesignField columnField(String columnName) {
        JRDesignField field = new JRDesignField();
        field.setName(columnName);
        field.setValueClass(Object.class);
        return field;
    }
}
