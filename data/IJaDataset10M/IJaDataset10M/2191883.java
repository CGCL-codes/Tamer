package br.absolut.negocio.produto;

import java.util.List;
import br.absolut.persistencia.produto.TipoProduto;

public interface SvnTipoProduto {

    public TipoProduto inclui(TipoProduto tipoProduto);

    public TipoProduto altera(TipoProduto tipoProduto);

    public List<TipoProduto> recuperaTodos();

    public TipoProduto recuperaPorId(Long codigo);

    public void exclui(Long codigo);
}
