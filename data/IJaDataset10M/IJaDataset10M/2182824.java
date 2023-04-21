package componente.consulta.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.TblConsultaHome;
import org.jgenesis.swing.JGButtonPanel;
import pattern.util.HCalendar;

/**
 * @author Henrick Daniel "esqueleto"
 *
 */
public class TelaHistoricoTensaoOcular {

    JFrame f;

    JLabel historicoDeTonometria;

    JButton retornar;

    JPanel panelPrincipal;

    JGButtonPanel buttonPanel;

    TblConsultaHome consultaHome = new TblConsultaHome();

    public String[] colunas = new String[] { "Data", "Hora", "T. Apl. OD", "T. Apl. OE", "T. Pneu. OD", "T. Pneu. OE" };

    public String[][] dados = new String[0][6];

    public DefaultTableModel modelo = new DefaultTableModel(dados, colunas);

    public JTable jtable = new JTable(modelo);

    public JScrollPane scrollPane = new JScrollPane(jtable);

    /**
	 * 
	 */
    public TelaHistoricoTensaoOcular(int medico, int prontuario) {
        super();
        f = new JFrame("Hist�rico de tonometria");
        historicoDeTonometria = new JLabel("HIST�RICO DE TONOMETRIA");
        retornar = new JButton("Retornar");
        retornar.setVerticalAlignment(JButton.CENTER);
        panelPrincipal = new JPanel(new GridLayout(3, 1, 0, 0));
        historicoDeTonometria.setVerticalAlignment(JLabel.CENTER);
        historicoDeTonometria.setHorizontalAlignment(JLabel.CENTER);
        panelPrincipal.add(historicoDeTonometria);
        panelPrincipal.add(scrollPane);
        ArrayList lista = consultaHome.listHistoricoTonometria(prontuario, medico);
        Iterator iterator = lista.iterator();
        modelo.setRowCount(0);
        String array[] = new String[6];
        Calendar aux = Calendar.getInstance();
        Object object[];
        while (iterator.hasNext()) {
            object = (Object[]) iterator.next();
            aux.setTime((Date) object[0]);
            array[0] = HCalendar.getStringddmmyyy(aux);
            array[1] = object[1].toString();
            array[2] = object[2].toString();
            array[3] = object[3].toString();
            array[4] = object[4].toString();
            array[5] = object[5].toString();
            modelo.addRow(array);
        }
        jtable.updateUI();
        jtable.repaint();
        buttonPanel = new JGButtonPanel();
        buttonPanel.add(retornar);
        panelPrincipal.add(buttonPanel);
        retornar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                f.setVisible(false);
                f = null;
            }
        });
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(panelPrincipal);
        f.setSize(600, 300);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new TelaHistoricoTensaoOcular(1, 1);
    }
}
