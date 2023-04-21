package br.usp.lexico;

import br.usp.maquinaestados.MaquinaEstados;
import br.usp.maquinaestados.Transicao;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author nathalia
 */
public class Lexico {

    private Hashtable asciiTable = new Hashtable();

    private ArrayList<Transicao> tabelaTransicoes = new ArrayList<Transicao>();

    private Hashtable tabelaSimbolos = new Hashtable();

    private Hashtable palavrasReservadas = new Hashtable();

    private ArrayList tabelaEstadosAceitacao = new ArrayList();

    private int estadoInicial = 0;

    private MaquinaEstados maquinaLexico = new MaquinaEstados(tabelaTransicoes, estadoInicial, tabelaEstadosAceitacao);

    public Lexico() {
        setAsciiTable();
        setTabelaTransicoes();
        setTabelaEstadosAceitacao();
        setPalavrasReservadas();
        maquinaLexico.setTabelaTransicoes(tabelaTransicoes);
        maquinaLexico.setTabelaEstadosAceitacao(tabelaEstadosAceitacao);
        maquinaLexico.setEstadoInicial(estadoInicial);
        maquinaLexico.setNome("Lexico");
        maquinaLexico.setReset(true);
    }

    public Hashtable getPalavrasReservadas() {
        return palavrasReservadas;
    }

    public void setPalavrasReservadas() {
        palavrasReservadas.put("program", "program");
        palavrasReservadas.put("end", "end");
        palavrasReservadas.put("int", "int");
        palavrasReservadas.put("float", "float");
        palavrasReservadas.put("string", "string");
        palavrasReservadas.put("boolean", "boolean");
        palavrasReservadas.put("input", "input");
        palavrasReservadas.put("output", "output");
        palavrasReservadas.put("while", "while");
        palavrasReservadas.put("endwhile", "endwhile");
        palavrasReservadas.put("do", "do");
        palavrasReservadas.put("for", "for");
        palavrasReservadas.put("beginfor", "beginfor");
        palavrasReservadas.put("endfor", "endfor");
        palavrasReservadas.put("if", "if");
        palavrasReservadas.put("then", "then");
        palavrasReservadas.put("else", "else");
        palavrasReservadas.put("endif", "endif");
        palavrasReservadas.put("and", "and");
        palavrasReservadas.put("or", "or");
        palavrasReservadas.put("xor", "xor");
        palavrasReservadas.put("not", "not");
        palavrasReservadas.put("return", "return");
        palavrasReservadas.put("true", "true");
        palavrasReservadas.put("false", "false");
        palavrasReservadas.put("function", "function");
        palavrasReservadas.put("beginfunction", "beginfunction");
        palavrasReservadas.put("endfunction", "endfunction");
        palavrasReservadas.put("+", "+");
        palavrasReservadas.put("-", "-");
        palavrasReservadas.put("*", "*");
        palavrasReservadas.put("/", "/");
        palavrasReservadas.put("%", "%");
        palavrasReservadas.put(";", ";");
        palavrasReservadas.put("=", "=");
        palavrasReservadas.put(",", ",");
        palavrasReservadas.put("(", "(");
        palavrasReservadas.put(")", ")");
        palavrasReservadas.put("{", "{");
        palavrasReservadas.put("}", "}");
        palavrasReservadas.put("[", "[");
        palavrasReservadas.put("]", "]");
        palavrasReservadas.put("<>", "<>");
        palavrasReservadas.put(">", ">");
        palavrasReservadas.put("<", "<");
        palavrasReservadas.put("<=", "<=");
        palavrasReservadas.put(">=", ">=");
        palavrasReservadas.put("==", "==");
    }

    public Hashtable getTabelaSimbolos() {
        return tabelaSimbolos;
    }

    public void setTabelaSimbolos(Hashtable tabelaSimbolos) {
        this.tabelaSimbolos = tabelaSimbolos;
    }

    public Hashtable getAsciiTable() {
        return asciiTable;
    }

    public void setAsciiTable() {
        asciiTable.put(9, "HT");
        asciiTable.put(10, "LF");
        asciiTable.put(32, "SPACE");
        asciiTable.put(33, "!");
        asciiTable.put(34, "\"");
        asciiTable.put(35, "#");
        asciiTable.put(36, "$");
        asciiTable.put(37, "%");
        asciiTable.put(38, "&");
        asciiTable.put(39, "");
        asciiTable.put(40, "(");
        asciiTable.put(41, ")");
        asciiTable.put(42, "*");
        asciiTable.put(43, "+");
        asciiTable.put(44, ",");
        asciiTable.put(45, "-");
        asciiTable.put(46, ".");
        asciiTable.put(47, "/");
        asciiTable.put(48, "0");
        asciiTable.put(49, "1");
        asciiTable.put(50, "2");
        asciiTable.put(51, "3");
        asciiTable.put(52, "4");
        asciiTable.put(53, "5");
        asciiTable.put(54, "6");
        asciiTable.put(55, "7");
        asciiTable.put(56, "8");
        asciiTable.put(57, "9");
        asciiTable.put(58, ":");
        asciiTable.put(59, ";");
        asciiTable.put(60, "<");
        asciiTable.put(61, "=");
        asciiTable.put(62, ">");
        asciiTable.put(63, "?");
        asciiTable.put(64, "@");
        asciiTable.put(65, "A");
        asciiTable.put(66, "B");
        asciiTable.put(67, "C");
        asciiTable.put(68, "D");
        asciiTable.put(69, "E");
        asciiTable.put(70, "F");
        asciiTable.put(71, "G");
        asciiTable.put(72, "H");
        asciiTable.put(73, "I");
        asciiTable.put(74, "J");
        asciiTable.put(75, "K");
        asciiTable.put(76, "L");
        asciiTable.put(77, "M");
        asciiTable.put(78, "N");
        asciiTable.put(79, "O");
        asciiTable.put(80, "P");
        asciiTable.put(81, "Q");
        asciiTable.put(82, "R");
        asciiTable.put(83, "S");
        asciiTable.put(84, "T");
        asciiTable.put(85, "U");
        asciiTable.put(86, "V");
        asciiTable.put(87, "W");
        asciiTable.put(88, "X");
        asciiTable.put(89, "Y");
        asciiTable.put(90, "Z");
        asciiTable.put(91, "[");
        asciiTable.put(92, "\\");
        asciiTable.put(93, "]");
        asciiTable.put(94, "^");
        asciiTable.put(95, "_");
        asciiTable.put(96, "`");
        asciiTable.put(97, "a");
        asciiTable.put(98, "b");
        asciiTable.put(99, "c");
        asciiTable.put(100, "d");
        asciiTable.put(101, "e");
        asciiTable.put(102, "f");
        asciiTable.put(103, "g");
        asciiTable.put(104, "h");
        asciiTable.put(105, "i");
        asciiTable.put(106, "j");
        asciiTable.put(107, "k");
        asciiTable.put(108, "l");
        asciiTable.put(109, "m");
        asciiTable.put(110, "n");
        asciiTable.put(111, "o");
        asciiTable.put(112, "p");
        asciiTable.put(113, "q");
        asciiTable.put(114, "r");
        asciiTable.put(115, "s");
        asciiTable.put(116, "t");
        asciiTable.put(117, "u");
        asciiTable.put(118, "v");
        asciiTable.put(119, "w");
        asciiTable.put(120, "x");
        asciiTable.put(121, "y");
        asciiTable.put(122, "z");
        asciiTable.put(123, "{");
        asciiTable.put(124, "|");
        asciiTable.put(125, "}");
        asciiTable.put(126, "~");
        asciiTable.put(127, "DEL");
    }

    public ArrayList getTabelaEstadosAceitacao() {
        return tabelaEstadosAceitacao;
    }

    public void setTabelaEstadosAceitacao() {
        tabelaEstadosAceitacao.add(2);
        tabelaEstadosAceitacao.add(4);
        tabelaEstadosAceitacao.add(7);
        tabelaEstadosAceitacao.add(9);
        tabelaEstadosAceitacao.add(11);
        tabelaEstadosAceitacao.add(12);
        tabelaEstadosAceitacao.add(13);
        tabelaEstadosAceitacao.add(15);
        tabelaEstadosAceitacao.add(16);
        tabelaEstadosAceitacao.add(18);
        tabelaEstadosAceitacao.add(19);
        tabelaEstadosAceitacao.add(20);
    }

    public int getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(int estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public MaquinaEstados getMaquinaLexico() {
        return maquinaLexico;
    }

    public void setMaquinaLexico(MaquinaEstados maquina) {
        this.maquinaLexico = maquina;
    }

    public ArrayList<Transicao> getTabelaTransicoes() {
        return tabelaTransicoes;
    }

    public void setTabelaTransicoes() {
        tabelaTransicoes.add(new Transicao(0, 1, "A", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "B", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "C", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "D", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "E", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "F", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "G", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "H", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "I", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "J", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "K", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "L", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "M", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "N", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "O", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "P", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "Q", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "R", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "S", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "T", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "U", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "V", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "W", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "X", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "Y", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "Z", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "a", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "b", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "c", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "d", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "e", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "f", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "g", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "h", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "i", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "j", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "k", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "l", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "m", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "n", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "o", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "p", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "q", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "r", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "s", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "t", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "u", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "v", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "w", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "x", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "y", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 1, "z", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "0", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "1", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "2", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "3", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "4", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "5", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "6", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "7", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "8", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 3, "9", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 20, "+", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, "-", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, "*", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, "/", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, "%", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, ";", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, ",", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, "(", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, ")", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, "{", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, "}", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, "[", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(0, 20, "]", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(1, 1, "A", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "B", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "C", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "D", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "E", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "F", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "G", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "H", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "I", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "K", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "L", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "M", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "N", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "O", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "P", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "Q", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "R", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "S", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "T", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "U", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "V", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "W", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "X", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "Y", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "Z", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "a", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "b", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "c", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "d", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "e", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "f", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "g", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "h", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "i", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "j", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "k", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "l", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "m", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "n", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "o", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "p", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "q", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "r", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "s", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "t", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "u", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "v", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "w", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "x", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "y", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "z", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "0", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "1", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "2", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "3", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "4", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "5", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "6", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "7", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "8", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 1, "9", "empilha"));
        tabelaTransicoes.add(new Transicao(1, 2, "other", "desempilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "0", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "1", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "2", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "3", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "4", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "5", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "6", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "7", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "8", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 3, "9", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 5, ".", "empilha"));
        tabelaTransicoes.add(new Transicao(3, 4, "other", "desempilhaNumero"));
        tabelaTransicoes.add(new Transicao(5, 6, "0", "empilha"));
        tabelaTransicoes.add(new Transicao(5, 6, "1", "empilha"));
        tabelaTransicoes.add(new Transicao(5, 6, "2", "empilha"));
        tabelaTransicoes.add(new Transicao(5, 6, "3", "empilha"));
        tabelaTransicoes.add(new Transicao(5, 6, "4", "empilha"));
        tabelaTransicoes.add(new Transicao(5, 6, "5", "empilha"));
        tabelaTransicoes.add(new Transicao(5, 6, "6", "empilha"));
        tabelaTransicoes.add(new Transicao(5, 6, "7", "empilha"));
        tabelaTransicoes.add(new Transicao(5, 6, "8", "empilha"));
        tabelaTransicoes.add(new Transicao(5, 6, "9", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "0", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "1", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "2", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "3", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "4", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "5", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "6", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "7", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "8", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 6, "9", "empilha"));
        tabelaTransicoes.add(new Transicao(6, 7, "other", "desempilhaNumero"));
        tabelaTransicoes.add(new Transicao(0, 10, "<", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 14, "=", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 15, ">", "empilha"));
        tabelaTransicoes.add(new Transicao(0, 8, "LF", "ignora"));
        tabelaTransicoes.add(new Transicao(0, 8, "SPACE", "ignora"));
        tabelaTransicoes.add(new Transicao(0, 8, "HT", "ignora"));
        tabelaTransicoes.add(new Transicao(8, 8, "LF", "ignora"));
        tabelaTransicoes.add(new Transicao(8, 8, "SPACE", "ignora"));
        tabelaTransicoes.add(new Transicao(8, 8, "HT", "ignora"));
        tabelaTransicoes.add(new Transicao(8, 9, "other", "desempilha"));
        tabelaTransicoes.add(new Transicao(10, 11, "=", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(10, 12, ">", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(10, 13, "other", "desempilha"));
        tabelaTransicoes.add(new Transicao(14, 15, "=", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(14, 16, "other", "desempilha"));
        tabelaTransicoes.add(new Transicao(17, 18, "=", "colocaTabelaSimbolos"));
        tabelaTransicoes.add(new Transicao(17, 19, "other", "desempilha"));
    }
}
