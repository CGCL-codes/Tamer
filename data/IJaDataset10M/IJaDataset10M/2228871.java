package persistencia;

import entidades.Funcionario;
import entidades.Produto;
import entidades.Venda;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import negocio.Central;
import negocio.Util;

public class VendaDAO {

    String csv = "bd/venda.csv";

    Central central;

    public VendaDAO(Central central) {
        this.central = central;
    }

    public ArrayList<Venda> carrega() throws FileNotFoundException, IOException {
        ArrayList<Venda> vendaArray = new ArrayList<Venda>();
        BufferedReader br = new BufferedReader(new FileReader(csv));
        String l = br.readLine();
        while (l != null) {
            String[] lsplit = l.split(";");
            Venda v = new Venda();
            v.setIdVenda(Integer.parseInt(lsplit[0].trim()));
            ArrayList<Produto> produtoArray = new ArrayList<Produto>();
            String[] pSplit = lsplit[1].split("#");
            for (String p : pSplit) {
                produtoArray.add(central.buscaProduto(Integer.parseInt(p)));
            }
            v.setProdutoArray(produtoArray);
            ArrayList<Integer> quantidadeArray = new ArrayList<Integer>();
            String[] qSplit = lsplit[2].split("#");
            for (String q : qSplit) {
                quantidadeArray.add(Integer.parseInt(q));
            }
            v.setQuantidadeArray(quantidadeArray);
            v.setCliente(central.buscaCliente(Integer.parseInt(lsplit[3])));
            v.setFuncionario(central.buscaFuncionario(Integer.parseInt(lsplit[4])));
            v.setPagamento(lsplit[5].trim());
            v.setStatus(lsplit[6].trim());
            vendaArray.add(v);
            l = br.readLine();
        }
        br.close();
        return vendaArray;
    }

    public void insere(Venda v) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
        bw.write(v.getIdVenda() + ";");
        int t = v.getProdutoArray().size();
        for (int i = 0; i < t; i++) {
            bw.write(v.getProdutoArray().get(i).getIdProduto() + "");
            if (i < t - 1) bw.write("#");
        }
        bw.write(";");
        for (int i = 0; i < t; i++) {
            bw.write(v.getQuantidadeArray().get(i) + "");
            if (i < t - 1) bw.write("#");
        }
        bw.write(";" + v.getCliente().getIdCliente() + ";" + v.getFuncionario().getIdFuncionario() + ";" + v.getPagamento() + ";" + v.getStatus() + "\n");
        bw.close();
    }

    public void remove(ArrayList<Venda> vendaArray, Venda vRemove) throws IOException {
        Util.criaArquivo(csv);
        for (Venda v : vendaArray) {
            if (!v.equals(vRemove)) insere(v);
        }
    }

    public void modifica(ArrayList<Venda> vendaArray) throws IOException {
        Util.criaArquivo(csv);
        for (Venda v : vendaArray) {
            insere(v);
        }
    }
}
