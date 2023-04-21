package services;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;
import objetos.CategoriaObj;
import objetos.ItemVendaObj;
import objetos.ProdutoObj;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import classes.ProdutoImpl;

public class ProdutoService {

    public OMElement cadastrarProduto(OMElement element) {
        ProdutoImpl pi = new ProdutoImpl();
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace ns_return = factory.createOMNamespace("return", "ret");
        OMElement return_element = factory.createOMElement("return", ns_return);
        ProdutoObj produto = new ProdutoObj();
        produto.loadOMElement(element);
        if (pi.cadastrarProduto(produto)) return_element.setText("true"); else return_element.setText("false");
        return return_element;
    }

    public OMElement atualizarProduto(OMElement element) {
        ProdutoImpl pi = new ProdutoImpl();
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace ns_return = factory.createOMNamespace("return", "ret");
        OMElement return_element = factory.createOMElement("return", ns_return);
        ProdutoObj produto = new ProdutoObj();
        produto.loadOMElement(element);
        if (pi.atualizarProduto(produto)) return_element.setText("true"); else return_element.setText("false");
        return return_element;
    }

    public OMElement desativarProduto(OMElement element) {
        ProdutoImpl pi = new ProdutoImpl();
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace ns_return = factory.createOMNamespace("return", "ret");
        OMElement return_element = factory.createOMElement("return", ns_return);
        if (pi.desativarProduto(Integer.parseInt(element.getText()))) return_element.setText("true"); else return_element.setText("false");
        return element;
    }

    public OMElement buscarProdutoPorId(OMElement element) {
        ProdutoImpl pi = new ProdutoImpl();
        ProdutoObj produto;
        produto = pi.buscarProdutoPorId(Integer.parseInt(element.getText()));
        return produto.toOMElement();
    }

    public OMElement buscarProdutosPorNome(OMElement element) {
        ProdutoImpl pi = new ProdutoImpl();
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns_ret = fac.createOMNamespace("return", "ret");
        OMElement return_element = fac.createOMElement("lista", ns_ret);
        LinkedList<ProdutoObj> produtos = new LinkedList<ProdutoObj>();
        produtos = pi.buscarProdutosPorNome(element.getText());
        Iterator<ProdutoObj> it = produtos.iterator();
        while (it.hasNext()) {
            return_element.addChild(it.next().toOMElement());
        }
        return return_element;
    }

    public OMElement buscarProdutosPorCategoria(OMElement element) {
        return element;
    }

    public OMElement buscarCategoriaDoProduto(OMElement element) {
        ProdutoImpl pi = new ProdutoImpl();
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns_ret = fac.createOMNamespace("return", "ret");
        OMElement return_element = fac.createOMElement("lista", ns_ret);
        ProdutoObj produto = new ProdutoObj();
        produto.loadOMElement(element);
        LinkedList<CategoriaObj> produtos = new LinkedList<CategoriaObj>();
        produtos = pi.buscarCategoriaDoProduto(produto);
        Iterator<CategoriaObj> it = produtos.iterator();
        while (it.hasNext()) {
            return_element.addChild(it.next().toOMElement());
        }
        return return_element;
    }

    public OMElement buscarProdutosPorMarca(OMElement element) {
        ProdutoImpl pi = new ProdutoImpl();
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns_ret = fac.createOMNamespace("return", "ret");
        OMElement return_element = fac.createOMElement("lista", ns_ret);
        LinkedList<ProdutoObj> produtos = new LinkedList<ProdutoObj>();
        produtos = pi.buscarProdutosPorMarca(element.getText());
        Iterator<ProdutoObj> it = produtos.iterator();
        while (it.hasNext()) {
            return_element.addChild(it.next().toOMElement());
        }
        return return_element;
    }

    public OMElement buscarProdutosPorPreco(OMElement element) {
        return element;
    }
}
