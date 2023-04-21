package se.kth.cid.edutella.extra;

import net.jxta.edutella.consumer.*;
import net.jxta.edutella.vocabulary.*;
import net.jxta.edutella.util.*;
import net.jxta.edutella.util.datamodel.*;
import com.hp.hpl.mesa.rdf.jena.model.*;
import com.hp.hpl.mesa.rdf.jena.mem.*;
import com.hp.hpl.mesa.rdf.jena.vocabulary.*;
import se.kth.cid.component.local.*;
import se.kth.cid.identity.*;
import se.kth.cid.component.MetaData;
import se.kth.cid.conzilla.app.*;
import se.kth.cid.conzilla.util.*;
import se.kth.cid.util.*;
import se.kth.cid.conzilla.menu.*;
import se.kth.cid.conzilla.tool.*;
import se.kth.cid.conzilla.controller.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.*;

public class EduResultDisplayer extends JPanel {

    public class ResponseReceiver implements ResultListener, Runnable {

        public void run() {
            try {
                showContent();
                resTable.updateUI();
            } catch (RDFException ex) {
                ex.printStackTrace();
            }
        }

        public void processResult(EduResultSet resultset) {
            QELResult qelresult = new QELResult(resultset);
            String resultString = qelresult.getRDFXML();
            System.out.println(resultString);
            getResults(resultset);
            SwingUtilities.invokeLater(this);
        }

        public void processFailure() {
        }

        ;
    }

    public ResponseReceiver createResultListener() {
        return new ResponseReceiver();
    }

    JTable resTable;

    Vector resultList;

    HashMap variableNames;

    Vector variableList;

    Resource resourceVar;

    List comps = new ArrayList();

    Set dispResources = new HashSet();

    MapController cont;

    public EduResultDisplayer(EduQuery query, Resource resourceVar, MapController cont) throws RDFException {
        this.cont = cont;
        this.resourceVar = resourceVar;
        resultList = new Vector();
        variableNames = new HashMap();
        variableList = new Vector();
        TableModel dataModel = new AbstractTableModel() {

            public String getColumnName(int col) {
                return "" + variableNames.get(variableList.get(col)) + " (" + variableList.get(col) + ")";
            }

            public int getColumnCount() {
                return variableList.size();
            }

            public int getRowCount() {
                return resultList.size();
            }

            public Object getValueAt(int row, int col) {
                return ((HashMap) resultList.get(row)).get(variableList.get(col));
            }
        };
        getVars(query);
        resTable = new JTable(dataModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(resTable), BorderLayout.CENTER);
    }

    void getVars(EduQuery query) {
        Iterator vars = query.getVariables();
        while (vars.hasNext()) {
            Resource var = (Resource) vars.next();
            String label = query.getVariableLabel(var);
            variableNames.put(var.getURI(), label);
            variableList.add(var.getURI());
        }
    }

    void getResults(EduResultSet rs) {
        try {
            for (Iterator i = rs.getResults(); i.hasNext(); ) {
                HashMap bindingMap = new HashMap();
                EduResultTuple rt = (EduResultTuple) i.next();
                for (Iterator j = rt.getBindings(); j.hasNext(); ) {
                    EduVariableBinding vb = (EduVariableBinding) j.next();
                    bindingMap.put(vb.getVariable().getURI(), vb.getValue());
                }
                resultList.add(bindingMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showContent() throws RDFException {
        Tracer.debug("Trying to show content");
        String titleVarUri = EdutellaExtra.CONZILLA_TITLE_QUERY;
        int j = 0;
        for (Iterator i = resultList.iterator(); i.hasNext(); ) {
            HashMap results = (HashMap) i.next();
            String resUri = ((RDFNode) results.get(resourceVar.getURI())).toString();
            if (!dispResources.contains(resUri)) {
                dispResources.add(resUri);
                RDFNode titleRes = (RDFNode) results.get(titleVarUri);
                String title = null;
                titleRes = (RDFNode) results.get(titleVarUri);
                if (titleRes == null) {
                    Tracer.debug("no binding for title found");
                    title = "Untitled";
                } else {
                    title = titleRes.toString();
                }
                se.kth.cid.component.Component c = new LocalComponent(URIClassifier.parseValidURI("urn:temp:component_" + j), URIClassifier.parseValidURI("urn:temp:component_" + j++), MIMEType.XML);
                c.getMetaData().set_general_title(new MetaData.LangStringType(new MetaData.LangString[] { new MetaData.LangString("", title) }));
                c.getMetaData().set_technical_location(new MetaData.Location[] { new MetaData.Location("URI", resUri) });
                c.getMetaData().set_technical_format(new MetaData.LangStringType(new MetaData.LangString[] { new MetaData.LangString("", "text/html") }));
                comps.add(c);
                Tracer.debug("Added: " + title);
            }
        }
        Tracer.debug("Showing content");
        cont.getContentSelector().selectContentFromSet((se.kth.cid.component.Component[]) comps.toArray(new se.kth.cid.component.Component[comps.size()]));
    }
}
