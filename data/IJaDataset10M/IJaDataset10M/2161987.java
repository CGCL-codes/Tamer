package br.ita.doacoes.view.campanha;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import br.ita.doacoes.core.campanha.CampVolDAOImpl;
import br.ita.doacoes.core.campanha.CampanhaDAOImpl;
import br.ita.doacoes.core.voluntarios.VoluntarioDAO;
import br.ita.doacoes.core.voluntarios.VoluntarioDAOImpl;
import br.ita.doacoes.domain.cadastrodoacoes.Pessoa;
import br.ita.doacoes.domain.campanha.CampVol;
import br.ita.doacoes.domain.campanha.Campanha;
import br.ita.doacoes.domain.voluntarios.Voluntario;
import br.ita.doacoes.view.voluntarios.CadastroPessoaFisica;
import br.ita.doacoes.view.voluntarios.CadastroVoluntario;
import java.awt.Font;
import java.awt.Point;
import javax.swing.SwingConstants;

public class JanelaVoluntario extends JFrame implements ActionListener, MouseListener {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JScrollPane scrollVoluntario = null;

    private MyTable tableVoluntario = null;

    DefaultTableModel model;

    private JButton buttonadd = null;

    private JButton buttonremv = null;

    private JButton buttonlist = null;

    private JButton buttonlistv = null;

    private JLabel labelCampanha = null;

    private Campanha camp;

    private List<Voluntario> vol;

    int ind;

    private JScrollPane scrollRelacao = null;

    int indr;

    private List<Voluntario> volr;

    DefaultTableModel modelr;

    private MyTable tableRelacao = null;

    private JButton buttonRel = null;

    private JLabel labelPessoas = null;

    /**
	 * This is the default constructor
	 */
    public JanelaVoluntario(Campanha c) {
        super();
        initialize();
        labelCampanha.setText("Volunt�rios da Campanha: " + c.getNome());
        ind = -1;
        indr = -1;
        camp = c;
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(466, 526);
        this.setResizable(false);
        this.setContentPane(getJContentPane());
        this.setTitle("Volunt�rios");
        this.setVisible(true);
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            labelPessoas = new JLabel();
            labelPessoas.setBounds(new Rectangle(73, 14, 291, 32));
            labelPessoas.setHorizontalAlignment(SwingConstants.CENTER);
            labelPessoas.setText("Volunt�rios Dispon�veis");
            labelCampanha = new JLabel();
            labelCampanha.setBounds(new Rectangle(46, 250, 360, 31));
            labelCampanha.setHorizontalAlignment(SwingConstants.CENTER);
            labelCampanha.setText("JLabel");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getScrollVoluntario(), null);
            jContentPane.add(getButtonadd(), null);
            jContentPane.add(getButtonremv(), null);
            jContentPane.add(getButtonlist(), null);
            jContentPane.add(getButtonlistv(), null);
            jContentPane.add(labelCampanha, null);
            jContentPane.add(getScrollRelacao(), null);
            jContentPane.add(getButtonRel(), null);
            jContentPane.add(labelPessoas, null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes scrollVoluntario	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getScrollVoluntario() {
        if (scrollVoluntario == null) {
            scrollVoluntario = new JScrollPane();
            scrollVoluntario.setBounds(new Rectangle(13, 51, 435, 128));
            scrollVoluntario.setViewportView(getTableVoluntario());
        }
        return scrollVoluntario;
    }

    /**
	 * This method initializes tableVoluntario	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getTableVoluntario() {
        if (tableVoluntario == null) {
            Object obj[][] = {};
            String[] title = { "Nome", "Disponibilidade" };
            model = new DefaultTableModel(obj, title);
            tableVoluntario = new MyTable(model);
            tableVoluntario.addMouseListener(this);
        }
        return tableVoluntario;
    }

    /**
	 * This method initializes buttonadd	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getButtonadd() {
        if (buttonadd == null) {
            buttonadd = new JButton();
            buttonadd.setFont(new Font("Dialog", Font.BOLD, 12));
            buttonadd.setSize(new Dimension(160, 37));
            buttonadd.setLocation(new Point(15, 184));
            buttonadd.setText("Gerenciar Volunt�rios");
            buttonadd.addActionListener(this);
        }
        return buttonadd;
    }

    /**
	 * This method initializes buttonremv	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getButtonremv() {
        if (buttonremv == null) {
            buttonremv = new JButton();
            buttonremv.setText("Remover");
            buttonremv.setSize(new Dimension(140, 40));
            buttonremv.setLocation(new Point(18, 441));
            buttonremv.addActionListener(this);
        }
        return buttonremv;
    }

    /**
	 * This method initializes buttonlist	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getButtonlist() {
        if (buttonlist == null) {
            buttonlist = new JButton();
            buttonlist.setText("Listar Todos");
            buttonlist.setSize(new Dimension(130, 37));
            buttonlist.setLocation(new Point(322, 184));
            buttonlist.addActionListener(this);
        }
        return buttonlist;
    }

    /**
	 * This method initializes buttonlistv	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getButtonlistv() {
        if (buttonlistv == null) {
            buttonlistv = new JButton();
            buttonlistv.setText("Listar");
            buttonlistv.setSize(new Dimension(140, 40));
            buttonlistv.setLocation(new Point(301, 442));
            buttonlistv.addActionListener(this);
        }
        return buttonlistv;
    }

    void populateTabela() {
        model.setRowCount(0);
        for (Object i : vol) {
            Object obj[] = new Object[2];
            obj[0] = ((Voluntario) i).getPessoa().getNome();
            if (((Voluntario) i).getDisponibilidade() == null) {
                obj[1] = null;
            } else {
                obj[1] = ((Voluntario) i).getDisponibilidade().getDescricao();
            }
            model.addRow(obj);
        }
    }

    void populateTabelar() {
        modelr.setRowCount(0);
        for (Object i : volr) {
            Object obj[] = new Object[2];
            obj[0] = ((Voluntario) i).getPessoa().getNome();
            if (((Voluntario) i).getDisponibilidade() == null) {
                obj[1] = null;
            } else {
                obj[1] = ((Voluntario) i).getDisponibilidade().getDescricao();
            }
            modelr.addRow(obj);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonRel) {
            if (ind < 0) return;
            CampVolDAOImpl dao = new CampVolDAOImpl(CampVol.class);
            List<CampVol> cl = dao.getBySQLQuery("SELECT DISTINCT x.id_campvol,x.id_campanha,x.id_voluntario FROM campanha_voluntario x, campanha c, voluntario v WHERE x.id_campanha = c.id_campanha AND x.id_voluntario = v.id_voluntario AND c.id_campanha = " + camp.getId_campanha() + " AND v.id_voluntario = " + vol.get(ind).getId_voluntario(), CampVol.class);
            if (!cl.isEmpty()) {
                return;
            }
            CampVol cvol = new CampVol();
            cvol.setCampanha(camp);
            cvol.setVoluntario(vol.get(ind));
            dao.insert(cvol);
        }
        if (e.getSource() == buttonremv) {
            if (indr < 0) return;
            CampVolDAOImpl dao = new CampVolDAOImpl(CampVol.class);
            dao.getBySQLQuery("DELETE FROM campanha_voluntario WHERE id_campanha = " + camp.getId_campanha() + " AND id_voluntario = " + volr.get(indr).getId_voluntario(), CampVol.class);
        }
        if (e.getSource() == buttonlist) {
            VoluntarioDAO dao = new VoluntarioDAOImpl(Voluntario.class);
            vol = dao.getList();
            this.populateTabela();
            ind = -1;
        }
        if (e.getSource() == buttonlistv) {
            CampVolDAOImpl dao = new CampVolDAOImpl(CampVol.class);
            List<CampVol> cvol;
            cvol = dao.getBySQLQuery("SELECT DISTINCT x.id_campvol,x.id_campanha,x.id_voluntario FROM campanha_voluntario x, campanha c, voluntario v WHERE x.id_campanha = c.id_campanha AND x.id_voluntario = v.id_voluntario AND c.id_campanha = " + camp.getId_campanha(), CampVol.class);
            volr = new ArrayList<Voluntario>();
            for (CampVol i : cvol) {
                volr.add(i.getVoluntario());
            }
            this.populateTabelar();
            indr = -1;
        }
        if (e.getSource() == buttonadd) {
            CadastroVoluntario cad = new CadastroVoluntario();
            JFrame jf = new JFrame();
            jf.add(cad);
            jf.setVisible(true);
            jf.setExtendedState(MAXIMIZED_BOTH);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == tableVoluntario) ind = tableVoluntario.getSelectedRow();
        if (e.getSource() == tableRelacao) indr = tableRelacao.getSelectedRow();
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    /**
	 * This method initializes scrollRelacao	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getScrollRelacao() {
        if (scrollRelacao == null) {
            scrollRelacao = new JScrollPane();
            scrollRelacao.setBounds(new Rectangle(9, 305, 442, 133));
            scrollRelacao.setViewportView(getTableRelacao());
        }
        return scrollRelacao;
    }

    /**
	 * This method initializes tableRelacao	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getTableRelacao() {
        if (tableRelacao == null) {
            Object obj[][] = {};
            String[] title = { "Nome", "Disponibilidade" };
            modelr = new DefaultTableModel(obj, title);
            tableRelacao = new MyTable(modelr);
            tableRelacao.addMouseListener(this);
        }
        return tableRelacao;
    }

    /**
	 * This method initializes buttonRel	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getButtonRel() {
        if (buttonRel == null) {
            buttonRel = new JButton();
            buttonRel.setText("Relacionar");
            buttonRel.setSize(new Dimension(130, 37));
            buttonRel.setLocation(new Point(182, 184));
            buttonRel.addActionListener(this);
        }
        return buttonRel;
    }
}
