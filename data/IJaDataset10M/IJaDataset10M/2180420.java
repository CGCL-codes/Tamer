package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Area;
import visao.TelaManterArea;
import visao.TelaNovaArea;
import dao.AreaDao;
import dao.AtorDao;

public class ControlaArea implements ActionListener {

    private static ControlaArea singleton = null;

    private static AreaDao areaDao;

    private TelaManterArea tma;

    private TelaNovaArea tna;

    private List<Area> areasInstanciadas;

    private String areaSelecionada = null;

    private AtorDao atorDao = AtorDao.getInstance();

    public static ControlaArea getInstance() {
        if (singleton == null) singleton = new ControlaArea();
        areaDao = AreaDao.getInstance();
        return singleton;
    }

    private ControlaArea() {
    }

    public void configuraTela(TelaManterArea tm) {
        this.tma = tm;
        tma.configuraOuvinte(this);
    }

    public void configuraNovaAreaTela(TelaNovaArea tn) {
        this.tna = tn;
        tna.configuraOuvinte(this);
    }

    public void habilitaTelaArea() {
        areasInstanciadas = areaDao.listar3();
        tma.escreveAreas(areasInstanciadas);
        tma.setResizable(false);
        tma.setModal(true);
        tma.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent eve) {
        String comando = eve.getActionCommand();
        if (comando.equals("area")) {
        } else if (comando.equals("carregaArea")) {
            comandoAtoresDaArea();
        } else if (comando.equals("insercao")) {
            boolean testa = comandoVerificaAreaCarregada();
            if (testa == true) {
                ControlaGECAU.getInstance().comandoEditaAtor(areaSelecionada, comando);
            }
        } else if (comando.equals("alteracao")) {
            boolean testa = comandoVerificaAreaCarregada();
            if (testa == true) {
                ControlaGECAU.getInstance().comandoEditaAtor(areaSelecionada, comando);
            }
        } else if (comando.equals("exclusao")) {
            boolean testa = comandoVerificaAreaCarregada();
            if (testa == true) {
                ControlaGECAU.getInstance().comandoEditaAtor(areaSelecionada, comando);
            }
        } else if (comando.equals("ok")) {
            comandoInsereNovaArea();
        }
    }

    private boolean comandoVerificaAreaCarregada() {
        if (areaSelecionada == null) {
            JOptionPane.showMessageDialog(null, "Carregue Primeiro a Area");
            return false;
        }
        return true;
    }

    private void comandoAtoresDaArea() {
        areaSelecionada = tma.selecionaArea();
        for (Area a : areasInstanciadas) {
            if (areaSelecionada.equals(a.getNome())) {
                tma.escreveAtoresArea(a);
            }
        }
    }

    public String getAreaSelecionada() {
        return areaSelecionada;
    }

    public void insereAreaAtravesMenuPrincipal() {
        tna.visualizaTela();
    }

    public void comandoInsereNovaArea() {
        Area area = new Area();
        String nome = tna.novaArea();
        if (nome == null) {
            JOptionPane.showMessageDialog(null, "Nome n�o digitado");
        } else {
            area.setNome(nome);
            AreaDao.getInstance().salvar(area);
            tna.dispose();
        }
    }
}
