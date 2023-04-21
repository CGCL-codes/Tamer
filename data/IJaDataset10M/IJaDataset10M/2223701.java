package br.com.dcjtravelme.action;

import br.com.dcjtravelme.model.Usuario;
import br.com.dcjtravelme.servico.HttpAcesso;
import br.com.dcjtravelme.util.CarregarDados;

/**
 * @author Marcelo de Castro / Sergio Watanabe
 */
public class SelecaoFormaPagtoAction extends AbstractAction {

    /** Creates a new instance of SelecaoFormaPagtoAction */
    public SelecaoFormaPagtoAction() {
    }

    public void okAction() {
        getMainMidlet().getCompra().setFormaPagto(getMainMidlet().getFormFormaPagto().getFormaPagtoSelecionada());
        Usuario usuario = getMainMidlet().getFormLogin().getUsuario();
        boolean idaEVolta = getMainMidlet().isIdaEVolta();
        String url = "acao=finalizaCompra&" + usuario.getLoginString() + "&vooIda=" + getMainMidlet().getCompra().getVooIda().getCodigo() + "&classeIda=" + getMainMidlet().getCompra().getClasseIda().getCodigo() + "&assentosIda=" + getMainMidlet().getCompra().getAssentosIdaAsString();
        if (idaEVolta) {
            url = url + "&vooRetorno=" + getMainMidlet().getCompra().getVooVolta().getCodigo() + "&classeRetorno=" + getMainMidlet().getCompra().getClasseVolta().getCodigo() + "&assentosRetorno=" + getMainMidlet().getCompra().getAssentosVoltaAsString();
        } else {
            url = url + "&vooRetorno=4&classeRetorno=1&assentosRetorno=0";
        }
        url = url + "&formaPagto=" + getMainMidlet().getCompra().getFormaPagto().getCodigo() + "&valor=" + getMainMidlet().getCompra().getValorAsString() + "&quantidade=" + getMainMidlet().getVooIda().getQuantidadesAsString();
        HttpAcesso http = new HttpAcesso(url);
        String resposta = http.getResposta();
        CarregarDados carregador = new CarregarDados(resposta);
        if (carregador.getMensagem() != null) {
            getMainMidlet().setMensagem(carregador.getMensagem());
        } else {
            getMainMidlet().setVoucher(carregador.getVoucherAsString());
            getMainMidlet().getFormVoucher().setImagens(carregador.getImagens());
            getMainMidlet().getDisplay().setCurrent(getMainMidlet().getFormVoucher());
        }
    }

    public void cancelAction() {
    }

    public void backAction() {
        getMainMidlet().getDisplay().setCurrent(getMainMidlet().getFormAssento());
    }
}
