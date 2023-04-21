package uk.ac.imperial.ma.metric.apps.databaseAdministration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class ResultPanel extends JPanel implements ActionListener {

    private DatabaseAdministrationGUI dag;

    private JScrollPane jsp;

    private JEditorPane jep;

    private Border border;

    private static final String HEADER = "<html><head><title>Database Administration Result Panel</title></head><body>";

    private static final String FOOTER = "</body></html>";

    private String body = "<p>Results</p>";

    public ResultPanel(DatabaseAdministrationGUI dag) {
        this.dag = dag;
        jep = new JEditorPane("text/html", "");
        jsp = new JScrollPane(jep);
        border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Results");
        setBorder(border);
        setLayout(new GridLayout(1, 1));
        add(jsp);
    }

    private void appendQuery(String query) {
        System.out.println("Query String: " + query);
        body = body.concat("<p><font color='green'>Query: <code>" + query + "</code></font></p>");
        update();
    }

    private void update() {
        System.out.println(HEADER);
        System.out.println(body);
        System.out.println(FOOTER);
        jep.setText(HEADER + body + FOOTER);
    }

    public void actionPerformed(ActionEvent ae) {
        System.out.println("In result panel action performed.");
        if (ae.getSource() == dag.qp.jbutQuery) {
            appendQuery(dag.qp.jtxtQuery.getText());
        }
    }
}
