package componente.cliente.control;

import java.util.Calendar;
import javax.swing.JOptionPane;
import model.TblCliente;
import model.TblClienteHome;
import model.TblConvenioHome;
import model.TblUsuarioHome;
import pattern.util.HCalendar;
import componente.cliente.view.LocateCliente;
import componente.cliente.view.PanelGeralPaciente;
import componente.cliente.view.PanelOutros;
import componente.cliente.view.PanelCadastroPaciente;
import componente.sistema.sessao.Sessao;

/**
 * @author Henrick Daniel
 *
 */
public class PesquisarClienteByCombo {

    /**
	 * 
	 */
    LocateCliente locateCliente;

    public PesquisarClienteByCombo(String tipoPesquisa) {
        super();
        locateCliente = new LocateCliente(this, tipoPesquisa);
    }

    public void trataMensagem(String mensagem) {
        TblClienteHome clienteHome = new TblClienteHome();
        TblUsuarioHome usuarioHome = new TblUsuarioHome();
        TblConvenioHome convenioHome = new TblConvenioHome();
        int linha = locateCliente.jtable.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(null, "� nessecario escolher um cliente");
        } else {
            int prontuario = Integer.parseInt(locateCliente.modelo.getValueAt(linha, 1) + "");
            int id_medico = usuarioHome.findByNome(locateCliente.jcbMedico.getSelectedItem() + "").getFkMedico();
            TblCliente entity = clienteHome.findByProntuario(prontuario, id_medico);
            PanelCadastroPaciente tela = ClienteControl.tela;
            PanelGeralPaciente painelGeral = (PanelGeralPaciente) tela.painelGeralPaciente;
            PanelOutros painelOutros = (PanelOutros) tela.painelOutros;
            tela.nome.setText("");
            tela.nprontuario.setText("");
            painelGeral.nome.setText("");
            painelGeral.matricula.setText("");
            painelGeral.bairro.setText("");
            painelGeral.endereco.setText("");
            painelGeral.cidade.setText("");
            painelGeral.estado.setText("");
            painelGeral.cep.setText("");
            painelGeral.telres.setText("");
            painelGeral.telcom.setText("");
            painelGeral.profissao.setText("");
            painelGeral.rg.setText("");
            painelGeral.cpf.setText("");
            painelGeral.naturalidade.setText("");
            painelGeral.datanasc.setText("");
            painelGeral.idade.setText("");
            painelGeral.sexo.setSelectedItem("");
            painelGeral.estcivil.setSelectedItem("");
            painelOutros.encaminhado.setText("");
            painelOutros.filiacao1.setText("");
            painelOutros.filiacao2.setText("");
            painelOutros.encaminhado.setText("");
            ;
            painelOutros.obs.setText("");
            tela.nome.setText(entity.getDeNome());
            tela.nprontuario.setText(entity.getId().getIdProntuario() + "");
            tela.medico.setSelectedItem(usuarioHome.findByFk_medico(id_medico).getDeNome());
            Sessao.setId_prontuario(entity.getId().getIdProntuario());
            Sessao.setId_medico(id_medico);
            painelGeral.bairro.setText(entity.getDeBairro());
            painelGeral.cep.setText(entity.getDeCep());
            painelGeral.cidade.setText(entity.getDeCidadeResidencia().trim());
            painelGeral.convenio.setSelectedItem(convenioHome.getNomeConvenioByCodConvenio(entity.getFkCodConvenio()));
            painelGeral.cpf.setText(entity.getDeCpf());
            Calendar aux = Calendar.getInstance();
            aux.setTime(entity.getDtDataNasc());
            painelGeral.datanasc.setText(HCalendar.getStringddmmyyy(aux));
            painelGeral.endereco.setText(entity.getDeEndereco());
            painelGeral.estado.setText(entity.getDeEstadoResidencia());
            painelGeral.estcivil.addItem(entity.getDeEstadoCivil());
            painelGeral.estcivil.repaint();
            painelGeral.estcivil.setSelectedItem(entity.getDeEstadoCivil());
            painelGeral.idade.setText(HCalendar.getIdade(HCalendar.setData(painelGeral.datanasc.getText())) + "");
            painelGeral.matricula.setText(entity.getDeMatricula());
            painelGeral.naturalidade.setText(entity.getDeNaturalidade());
            painelGeral.nome.setText(entity.getDeTitular());
            painelGeral.profissao.setText(entity.getDeProfissao());
            painelGeral.rg.setText(entity.getDeRg());
            painelGeral.sexo.setSelectedItem(entity.getDeSexo());
            if (entity.getDeSexo().toString().equalsIgnoreCase("f") || entity.getDeSexo().toString().equalsIgnoreCase("F")) {
                painelGeral.sexo.setSelectedItem("Feminino");
            } else {
                painelGeral.sexo.setSelectedItem("Masculino");
            }
            painelGeral.telcom.setText(entity.getDeTelefoneComerc().trim());
            painelGeral.telres.setText(entity.getDeTelefone().trim());
            painelOutros.encaminhado.setText(entity.getDeEncaminhado());
            painelOutros.filiacao1.setText(entity.getDeMae());
            painelOutros.filiacao2.setText(entity.getDePai());
            painelOutros.obs.setText(entity.getDeObs());
            painelGeral.idade.setText(HCalendar.getIdade(HCalendar.setData(painelGeral.datanasc.getText())) + "");
            locateCliente.frame.setVisible(false);
        }
    }
}
