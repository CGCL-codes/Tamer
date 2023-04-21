package huffman;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class HuffmanUnCompress {

    private List<Nodo> nodosDicionario;

    private String frase;

    private int numSimbolos;

    private FilaDePrioridade filaPrioridade;

    private List<String> alfabeto;

    private List<Nodo> nodosAlfabeto;

    private String fraseOriginal;

    public HuffmanUnCompress(String frase) {
        alfabeto = new ArrayList<String>();
        nodosAlfabeto = new ArrayList<Nodo>();
        this.frase = frase;
        String dictFrase = "";
        Character cExt = frase.charAt(0);
        String sExt = cExt.toString();
        int tamanhoExt = Integer.parseInt(sExt) + 1;
        for (int i = tamanhoExt; i < frase.length(); i++) {
            String aux = frase.substring(i, i + 6);
            if (aux.equals("-D-AFW")) {
                dictFrase = frase.substring(i + 6, frase.length());
                frase = frase.substring(tamanhoExt, i);
                break;
            }
        }
        geraNodosDict(dictFrase);
        this.frase = recuperaBinarios(frase);
        this.filaPrioridade = new FilaDePrioridade(nodosDicionario);
        this.numSimbolos = this.filaPrioridade.getSize();
        for (int i = 0; i < this.numSimbolos - 1; i++) {
            Nodo esq = this.filaPrioridade.extractMin();
            Nodo dir = this.filaPrioridade.extractMin();
            Nodo nodo = new Nodo("", esq.getFrequencia() + dir.getFrequencia());
            nodo.setEsq(esq);
            nodo.setDir(dir);
            this.filaPrioridade.insert(nodo);
        }
    }

    private void geraNodosDict(String dictFrase) {
        this.nodosDicionario = new ArrayList<Nodo>();
        Nodo nodo;
        String[] split;
        String aux;
        Character auxValor;
        String valor;
        String sFrequencia;
        int iFrequencia;
        if (dictFrase != null && !dictFrase.equals("")) {
            split = dictFrase.split("\\|{2}");
            for (int i = 0; i < split.length; i++) {
                aux = split[i];
                auxValor = aux.charAt(0);
                valor = auxValor.toString();
                sFrequencia = aux.substring(1);
                iFrequencia = Integer.parseInt(sFrequencia);
                nodo = new Nodo(valor, iFrequencia);
                nodosDicionario.add(nodo);
            }
        }
    }

    private String recuperaBinarios(String frase) {
        StringBuffer sb = new StringBuffer(frase.length() * 8);
        String fraseBinaria;
        String sBinario;
        char c;
        Character cZeros = frase.charAt(frase.length() - 1);
        String sZeros = cZeros.toString();
        int zeros = Integer.parseInt(sZeros);
        frase = frase.substring(0, frase.length() - 1);
        for (int i = 0; i < frase.length(); i++) {
            c = frase.charAt(i);
            sBinario = Integer.toBinaryString(c);
            int tamanho = 8 - sBinario.length();
            switch(tamanho) {
                case 1:
                    sBinario = "0" + sBinario;
                    break;
                case 2:
                    sBinario = "00" + sBinario;
                    break;
                case 3:
                    sBinario = "000" + sBinario;
                    break;
                case 4:
                    sBinario = "0000" + sBinario;
                    break;
                case 5:
                    sBinario = "00000" + sBinario;
                    break;
                case 6:
                    sBinario = "000000" + sBinario;
                    break;
                case 7:
                    sBinario = "0000000" + sBinario;
                    break;
            }
            sb.append(sBinario);
        }
        fraseBinaria = sb.toString();
        fraseBinaria = fraseBinaria.substring(0, fraseBinaria.length() - zeros);
        return fraseBinaria;
    }

    private void geraCodeword(Nodo nodo, String codeword) {
        if (nodo != null) {
            if (!nodo.getValor().equals("")) {
                this.alfabeto.add(nodo.getValor());
                this.nodosAlfabeto.add(nodo);
                nodo.setCodeword(codeword);
            }
            geraCodeword(nodo.getEsq(), codeword + "0");
            geraCodeword(nodo.getDir(), codeword + "1");
        }
    }

    private void montaFrase(Nodo nodo) {
        StringBuffer sb = new StringBuffer(frase.length());
        String sAux;
        int iAux;
        for (int i = 0; i < frase.length(); i++) {
            sAux = String.valueOf(frase.charAt(i));
            iAux = Integer.parseInt(sAux);
            switch(iAux) {
                case 1:
                    nodo = nodo.getDir();
                    if (!nodo.getValor().equals("")) {
                        sb.append(nodo.getValor());
                        nodo = this.filaPrioridade.getMenor();
                    }
                    break;
                case 0:
                    nodo = nodo.getEsq();
                    if (!nodo.getValor().equals("")) {
                        sb.append(nodo.getValor());
                        nodo = this.filaPrioridade.getMenor();
                    }
                    break;
            }
        }
        fraseOriginal = sb.toString();
    }

    public String geraFrase() {
        geraCodeword(this.filaPrioridade.getMenor(), "");
        montaFrase(this.filaPrioridade.getMenor());
        return this.fraseOriginal;
    }
}
