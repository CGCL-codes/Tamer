package practica1;

import java.util.Stack;
import java.util.Vector;

/**
 *
 * @author Monsalve Solís
 */
public class Thompson {

    private Nodo p;

    private Nodo automata;

    private int i = 0;

    private String expresion, respuesta = "(";

    private int nombre = 0;

    private Vector terminales;

    Thompson(String expr) {
        terminales = new Vector();
        this.expresion = expr;
        respuesta = respuesta.concat(expr + ")");
        expresion = respuesta;
        automata = conectar();
        p.unVisit();
        Tabla e = new Tabla();
        e.pintarMatriz(p, nombre, terminales);
        Nodo.numeroDeDatos = 0;
        e.setVisible(true);
        e.pintarMatrizND(cierreLambda(), terminales, nombre);
    }

    public Nodo conectar() {
        if (isTerminal(expresion.charAt(i))) {
            p = conectarTerminal(expresion.charAt(i));
            i++;
        } else if (expresion.charAt(i) == '(') {
            i++;
            p = expresionRegular();
            if (expresion.charAt(i) != ')') System.out.println("ERROR");
            i++;
        }
        if (i < expresion.length()) {
            if (expresion.charAt(i) == '*') {
                p = conectarAsterisco(p);
                i++;
            }
            if (expresion.charAt(i) == '+') {
                p = conectarMas(p);
                i++;
            }
        }
        return p;
    }

    public Nodo expresionRegular() {
        Nodo p1 = concatenacion();
        while (i < expresion.length() && expresion.charAt(i) == '|') {
            i++;
            Nodo p2 = concatenacion();
            p1 = conectarUnion(p1, p2);
        }
        return p1;
    }

    public Nodo concatenacion() {
        Nodo p1 = conectar();
        while (isTerminal(expresion.charAt(i)) || expresion.charAt(i) == '(') {
            Nodo p2 = conectar();
            p1 = conectarPor(p1, p2);
        }
        return p1;
    }

    public Nodo conectarTerminal(char dato1) {
        Nodo inicial = new Nodo(dato1, '-');
        nombre++;
        inicial.setName(nombre);
        nombre++;
        Nodo aceptacion = new Nodo('-', '-');
        aceptacion.setName(nombre);
        inicial.setLiga1(aceptacion);
        inicial.setLiga2(null);
        return inicial;
    }

    public Nodo conectarAsterisco(Nodo p) {
        Nodo inicial = new Nodo('~', '~');
        Nodo aceptacion = new Nodo('-', '-');
        nombre = nombre + 2;
        if (p.getName() != 1) {
            inicial.setName(p.getName());
        } else {
            inicial.setName(1);
        }
        aceptacion.setName(nombre);
        inicial.setLiga1(p);
        p.ultimo().setDato2('~');
        p.ultimo().setDato1('~');
        inicial.setLiga2(aceptacion);
        p.ultimo().setLiga2(p);
        p.ultimo().setLiga1(aceptacion);
        renombrar(inicial);
        p = inicial;
        return p;
    }

    public Nodo conectarMas(Nodo p) {
        Nodo inicial = new Nodo('~', '-');
        Nodo aceptacion = new Nodo('-', '-');
        nombre = nombre + 2;
        if (p.getName() != 1) {
            inicial.setName(p.getName());
        } else {
            inicial.setName(1);
        }
        aceptacion.setName(nombre);
        inicial.setLiga1(p);
        p.ultimo().setDato1('~');
        p.ultimo().setDato2('~');
        p.ultimo().setLiga2(p);
        p.ultimo().setLiga1(aceptacion);
        renombrar(inicial);
        p = inicial;
        return p;
    }

    public Nodo conectarUnion(Nodo nodo1, Nodo nodo2) {
        Nodo inicial = new Nodo('~', '~');
        nombre = nombre + 2;
        if (nodo1.getName() != 1) {
            inicial.setName(nodo1.getName());
        } else {
            inicial.setName(1);
        }
        inicial.setLiga1(nodo1);
        inicial.setLiga2(nodo2);
        Nodo aceptacion = new Nodo('-', '-');
        aceptacion.setName(nombre);
        nodo1.ultimo().setDato1('~');
        nodo2.ultimo().setDato1('~');
        nodo1.ultimo().setLiga1(aceptacion);
        nodo2.ultimo().setLiga1(aceptacion);
        aceptacion.setVisit(true);
        renombrar(inicial);
        return inicial;
    }

    public Nodo conectarPor(Nodo nodo1, Nodo nodo2) {
        nodo1.ultimo().setDato1('~');
        nodo1.ultimo().setLiga1(nodo2);
        return nodo1;
    }

    private boolean isTerminal(char car) {
        if (car == '*' || car == '+' || car == '(' || car == ')' || car == '|') return false;
        if (!(terminales.contains(car))) terminales.add(car);
        return true;
    }

    private void renombrar(Nodo aux) {
        p = aux;
        p.unVisit();
        Stack pila = new Stack();
        if (aux.getLiga2() != null) pila.push(aux.getLiga2());
        aux = aux.getLiga1();
        while (!pila.isEmpty() || aux != null) {
            if (aux.getLiga2() != null && !aux.getLiga2().isVisit()) {
                pila.push(aux.getLiga2());
            }
            if (aux.getName() != nombre && !aux.isVisit()) {
                aux.setName(aux.getName() + 1);
                aux.setVisit(true);
            }
            aux = aux.getLiga1();
            if (!pila.isEmpty() && aux == null) {
                aux = (Nodo) pila.pop();
            }
        }
        p.unVisit();
    }

    public Vector cierreLambda() {
        Nodo aux = p;
        aux.unVisit();
        Vector[] conjunto = new Vector[nombre];
        Stack pila = new Stack();
        while (!pila.isEmpty() || aux != null) {
            if (aux.getLiga2() != null && !aux.getLiga2().isVisit()) {
                pila.push(aux.getLiga2());
            }
            if (aux != null && !aux.isVisit()) {
                if (conjunto[aux.getName() - 1] == null) {
                    conjunto[aux.getName() - 1] = recorrerLambda(aux);
                    aux = p;
                    aux.unVisit();
                    pila.removeAllElements();
                    if (aux.getLiga2() != null && !aux.getLiga2().isVisit()) {
                        pila.push(aux.getLiga2());
                    }
                }
                aux.setVisit(true);
            }
            aux = aux.getLiga1();
            if (aux == null && !pila.isEmpty()) {
                aux = (Nodo) pila.pop();
            }
        }
        for (int po = 0; po < conjunto.length; po++) {
            Vector porsi = (Vector) conjunto[po];
            int estado = po + 1;
            System.out.print("ESTADO " + estado + " = ");
            for (int k = 0; k < porsi.size(); k++) {
                System.out.print(((Nodo) porsi.elementAt(k)).getName() + "  ");
            }
            System.out.println("");
        }
        Vector subRaiz = new Vector();
        Vector raiz = new Vector();
        subRaiz.add(conjunto[0]);
        raiz.add(subRaiz);
        System.out.println("terminales " + terminales);
        for (int j = 0; j < raiz.size(); j++) {
            Vector aux2 = (Vector) ((Vector) raiz.elementAt(j)).elementAt(0);
            subRaiz = (Vector) raiz.elementAt(j);
            for (int q = 0; q < terminales.size(); q++) {
                Vector subTerminal = new Vector();
                for (int k = 0; k < aux2.size(); k++) {
                    Nodo siliar = recorrerTerminal((Nodo) aux2.elementAt(k), (String.valueOf(terminales.elementAt(q))).charAt(0));
                    if (siliar != null) {
                        Vector delconj = conjunto[siliar.getName() - 1];
                        for (int r = 0; r < delconj.size(); r++) {
                            if (!subTerminal.contains(delconj.elementAt(r))) {
                                subTerminal.add(delconj.elementAt(r));
                            }
                        }
                    }
                }
                boolean repite = false;
                int contar = 0;
                int transi = 0;
                for (int s = 0; s < raiz.size(); s++) {
                    Vector comparacion = (Vector) ((Vector) raiz.elementAt(s)).elementAt(0);
                    for (int g = 0; g < subTerminal.size(); g++) {
                        if (comparacion.contains(subTerminal.elementAt(g))) {
                            contar++;
                        }
                    }
                    if (contar == subTerminal.size()) {
                        repite = true;
                        transi = s + 1;
                        break;
                    }
                    System.out.println("comparo " + comparacion + " con  " + subTerminal + "  = " + repite);
                    System.out.println(contar + "SIZE " + subTerminal.size());
                    contar = 0;
                }
                System.out.println("");
                if (!repite && !subTerminal.isEmpty()) {
                    System.out.println("entro repite");
                    Vector subRaizSig = new Vector();
                    subRaizSig.add(subTerminal);
                    raiz.add(subRaizSig);
                    transi = raiz.size();
                }
                if (!subTerminal.isEmpty()) {
                    subRaiz.add(terminales.elementAt(q));
                    subRaiz.add(transi);
                }
            }
        }
        for (int j = 0; j < raiz.size(); j++) {
            Vector e = (Vector) ((Vector) raiz.elementAt(j)).elementAt(0);
            int estado = j + 1;
            System.out.println("ESTADOoo " + estado + " = " + e);
            System.out.println((Vector) raiz.elementAt(j));
        }
        return raiz;
    }

    private Nodo recorrerTerminal(Nodo aux, char terminal) {
        if (aux.getDato1() == terminal) {
            return aux.getLiga1();
        }
        return null;
    }

    private Vector recorrerLambda(Nodo aux) {
        Vector cierres = new Vector();
        aux.unVisit();
        Stack pila = new Stack();
        while (!pila.isEmpty() || aux.getDato1() == '~' || aux.getDato2() == '~') {
            if (aux.getLiga2() != null && !aux.getLiga2().isVisit() && aux.getDato2() == '~') {
                pila.push(aux.getLiga2());
            }
            cierres.add(aux);
            if (aux != null && !aux.isVisit() && aux.getDato1() == '~') {
                aux.setVisit(true);
                aux = aux.getLiga1();
            } else {
                if (!pila.isEmpty()) {
                    aux = (Nodo) pila.pop();
                }
            }
        }
        cierres.add(aux);
        return cierres;
    }
}
