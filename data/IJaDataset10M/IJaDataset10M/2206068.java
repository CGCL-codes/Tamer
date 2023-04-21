package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Area;
import modelo.Projeto;
import visao.TelaAbrirProjeto;
import visao.TelaCadastroProjeto;
import visao.TelaPrincipal;
import dao.AreaDao;
import dao.ProjetoDao;

public class ControlaProjeto implements ActionListener {

    private static ControlaProjeto singleton = null;

    private TelaCadastroProjeto tcp;

    private TelaAbrirProjeto tap;

    private TelaPrincipal tp;

    String areas;

    public static ControlaProjeto getInstance() {
        if (singleton == null) singleton = new ControlaProjeto();
        return singleton;
    }

    private ControlaProjeto() {
    }

    public void configuraTela(TelaCadastroProjeto tcp) {
        this.tcp = tcp;
        tcp.configuraOuvinte(this);
        tcp.setResizable(false);
    }

    public void habilitaTelaCadastro() {
        List<Area> areas = AreaDao.getInstance().listar3();
        tcp.escreveAreas(areas);
        tcp.setModal(true);
        tcp.setResizable(false);
        tcp.setVisible(true);
    }

    public void configuraTelaAbrirProjeto(TelaAbrirProjeto tapr) {
        this.tap = tapr;
        tap.configuraOuvinte(this);
        tap.setResizable(false);
    }

    public void habilitaTelaAbrirProjeto() {
        List<Projeto> projeto = ProjetoDao.getInstance().listar();
        tap.escreveProjetos(projeto);
        tap.setModal(true);
        tap.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        String comando = evento.getActionCommand();
        if (comando.equals("novoProjeto")) {
            novoProjeto();
        } else if (comando.equals("abrirProjeto")) {
            carregarProjeto();
        }
    }

    private void carregarProjeto() {
        String nomeProjeto = tap.recebeProjeto();
        Projeto p = (Projeto) ProjetoDao.getInstance().listarProjetosNome(nomeProjeto);
        tap.dispose();
        JOptionPane.showMessageDialog(null, "Projeto carregado com sucesso");
        ControlaGECAU.getInstance().recebeControleProjetoAtual(p);
    }

    private void novoProjeto() {
        String nomeProjeto = tcp.nomeProjeto();
        String areaEscolhida = tcp.getAreaEscolhidaProjeto();
        Area areaCorreta = null;
        List<Area> l = AreaDao.getInstance().listar3();
        for (Area a : l) {
            if (areaEscolhida.equals(a.getNome())) {
                areaCorreta = a;
            }
        }
        Projeto p = new Projeto();
        p.setNome(nomeProjeto);
        p.setArea(areaCorreta);
        ProjetoDao.getInstance().salvar(p);
        ControlaGECAU.getInstance().recebeControleProjetoAtual(p);
    }
}
