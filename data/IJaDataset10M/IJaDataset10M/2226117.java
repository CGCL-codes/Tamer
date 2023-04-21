package ucm.si.TeoriaActividad.Interprete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;
import ucm.si.Checker.Interprete;
import ucm.si.Checker.Modelo;
import ucm.si.Checker.util.StateLabeledList;
import ucm.si.TeoriaActividad.GUI.DrawerActividad;
import ucm.si.TeoriaActividad.actividad.*;
import ucm.si.TeoriaActividad.estado.EstadoTA;
import ucm.si.TeoriaActividad.estado.ItemRole;
import ucm.si.TeoriaActividad.item.*;
import ucm.si.animadorGUI.Drawer;
import ucm.si.animadorGUI.util.Launcher;
import ucm.si.basico.ecuaciones.*;

/**
 *
 * @author José Antonio
 */
public class Pruebas implements Interprete<EstadoTA>, IInterprete {

    public ItemGenerator itemGen;

    public ActividadGenerator activGen;

    public String[] actividades;

    public String[] items;

    private ArrayList<String> l;

    private LinkedList<String> actividadesOrdenadas;

    private TreeMap<String, Set<String>> tabla;

    public Pruebas() {
        Item item1 = new Item("1");
        Item item2 = new Item("2");
        Item item3 = new Item("3");
        Item item4 = new Item("4");
        Item item5 = new Item("5");
        Item item6 = new Item("6");
        itemGen = ItemGenerator.getReference();
        Item[] listaItem1 = { item1, item2 };
        Item[] listaItem2 = { item2, item4 };
        Item[] listaItem3 = { item3, item5 };
        Item[] listaItem4 = { item3, item4 };
        Actividad actividad1 = new Actividad("A1", listaItem1, new Item[0], new Item[0], new Item[0], new Item[0], new Item[0], new Item[] { item4 }, new Conditions[0]);
        Actividad actividad2 = new Actividad("A2", listaItem2, new Item[0], new Item[0], new Item[0], new Item[0], new Item[] { item4 }, new Item[0], new Conditions[0]);
        Actividad actividad3 = new Actividad("A3", listaItem3, new Item[0], new Item[0], new Item[0], new Item[0], new Item[0], new Item[0], new Conditions[0]);
        Actividad actividad4 = new Actividad("A4", listaItem4, new Item[0], new Item[0], new Item[0], new Item[0], new Item[0], new Item[0], new Conditions[0]);
        actividad1.addActividadHija(actividad2);
        actividad1.addActividadHija(actividad4);
        activGen = ActividadGenerator.getReference();
        try {
            itemGen.addItem(item1);
            itemGen.addItem(item2);
            itemGen.addItem(item3);
            itemGen.addItem(item4);
            itemGen.addItem(item5);
            itemGen.addItem(item6);
            activGen.addActividad(actividad1);
            activGen.addActividad(actividad2);
            activGen.addActividad(actividad3);
            activGen.addActividad(actividad4);
            actividades = activGen.getConjunto().keySet().toArray(new String[0]);
            items = itemGen.getItems();
            actividadesOrdenadas = new LinkedList<String>();
            for (int i = 0; i < actividades.length; i++) {
                Actividad a = activGen.getItem(actividades[i]);
                if (a.getPadre() == null) {
                    actividadesOrdenadas.add(a.getNombre());
                }
            }
            int a = 0;
            while (actividadesOrdenadas.size() < actividades.length) {
                Actividad act = activGen.getItem(actividadesOrdenadas.get(a));
                Set<Actividad> setaux = act.getActividadesHijas();
                if (!setaux.isEmpty()) {
                    for (Iterator<Actividad> it = setaux.iterator(); it.hasNext(); ) {
                        actividadesOrdenadas.addLast(it.next().getNombre());
                    }
                }
                a++;
            }
            int numact = actividadesOrdenadas.size();
            tabla = new TreeMap<String, Set<String>>();
            for (int i = 0; i < numact; i++) {
                Actividad a1 = activGen.getItem(actividadesOrdenadas.get(i));
                Item[] s1 = a1.getItemNecesarios();
                TreeSet<String> conjComp = new TreeSet<String>();
                for (int j = 0; j < numact; j++) {
                    if (i != j) {
                        Actividad a2 = activGen.getItem(actividadesOrdenadas.get(j));
                        if (!a1.parienteDe(a2)) {
                            Item[] s2 = a2.getItemNecesarios();
                            boolean incompatibles = false;
                            for (int k = 0; !incompatibles && k < s1.length; k++) {
                                Item e1 = s1[k];
                                for (int m = 0; !incompatibles && m < s2.length; m++) {
                                    Item e2 = s2[m];
                                    if (e1.compareTo(e2) == 0) {
                                        incompatibles = true;
                                    }
                                }
                            }
                            if (!incompatibles) {
                                conjComp.add(a2.getNombre());
                            }
                        } else {
                            conjComp.add(a2.getNombre());
                        }
                    }
                }
                tabla.put(a1.getNombre(), conjComp);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<EstadoTA> iniciales() {
        ListaEstadosActividades lEstAct = new ListaEstadosActividades();
        for (int i = 0; i < actividades.length; i++) {
            lEstAct.addEstado(actividades[i], EstadoActividad.Waiting);
        }
        ListaEstadosItems lEstItems = new ListaEstadosItems();
        for (int i = 0; i < items.length; i++) {
            lEstItems.addEstado(items[i], EstadoItem.FREE);
        }
        EstadoTA estadoIni = new EstadoTA(lEstItems, lEstAct, new TreeMap<String, Set<String>>());
        List<EstadoTA> laux = new ArrayList<EstadoTA>();
        laux.add(estadoIni);
        return laux;
    }

    public List<EstadoTA> transitar(EstadoTA state) {
        EstadoTA estadoini = new EstadoTA(state);
        for (Iterator<String> it = actividadesOrdenadas.iterator(); it.hasNext(); ) {
            String a = it.next();
            if (actividadEjecutada(a, estadoini)) {
                estadoini.actividades.setEstado(a, EstadoActividad.Finalized);
                if (estadoini.propietarias.containsKey(a)) {
                    for (Iterator<String> it2 = estadoini.propietarias.get(a).iterator(); it2.hasNext(); ) {
                        String item = it2.next();
                        if (!itemHeredado(a, item, estadoini)) {
                            estadoini.items.setEstado(item, EstadoItem.FREE);
                        }
                    }
                    estadoini.propietarias.remove(a);
                }
                Item[] itemsToDispose = activGen.getItem(a).getItemToDispose();
                for (int i = 0; i < itemsToDispose.length; i++) {
                    String item = itemsToDispose[i].getClave();
                    if (!itemHeredado(a, item, estadoini)) {
                        estadoini.items.setEstado(item, EstadoItem.DISPOSED);
                    }
                }
                Item[] itemsToGenerate = activGen.getItem(a).getItemToGenerate();
                for (int i = 0; i < itemsToGenerate.length; i++) {
                    String item = itemsToGenerate[i].getClave();
                    estadoini.items.setEstado(item, EstadoItem.FREE);
                }
            }
        }
        List<EstadoTA> laux = backtracking(estadoini);
        if (laux.contains(state)) {
            laux.remove(state);
        }
        return laux;
    }

    public StateLabeledList<EstadoTA> transitarConEtiqueta(EstadoTA state) {
        List<EstadoTA> laux = this.transitar(state);
        ArrayList<String> laux2 = new ArrayList<String>();
        for (Iterator<EstadoTA> it = laux.iterator(); it.hasNext(); ) {
            EstadoTA e = it.next();
            laux2.add(nombreTransicion(state, e));
        }
        return new StateLabeledList<EstadoTA>(laux, laux2);
    }

    public List<String> dameTransiciones() {
        if (l == null) {
            Queue<EstadoTA[]> q = new LinkedBlockingQueue<EstadoTA[]>();
            TreeSet<EstadoTA[]> ts = new TreeSet<EstadoTA[]>(new Comparator() {

                public int compare(Object arg0, Object arg1) {
                    EstadoTA[] c1 = (EstadoTA[]) arg0;
                    EstadoTA[] c2 = (EstadoTA[]) arg1;
                    int a = c1[0].compareTo(c2[0]);
                    if (a != 0) {
                        return a;
                    } else {
                        return c1[1].compareTo(c2[1]);
                    }
                }
            });
            for (EstadoTA e : this.iniciales()) {
                q.add(new EstadoTA[] { null, e });
            }
            l = new ArrayList<String>();
            while (!q.isEmpty()) {
                EstadoTA[] arraye = q.poll();
                EstadoTA epadre = arraye[0];
                EstadoTA ehijo = arraye[1];
                if (epadre == null) {
                    for (EstadoTA eaux : transitar(ehijo)) {
                        q.add(new EstadoTA[] { ehijo, eaux });
                    }
                } else {
                    EstadoTA[] trans = new EstadoTA[] { epadre, ehijo };
                    if (!ts.contains(trans)) {
                        String s = nombreTransicion(epadre, ehijo);
                        if (!l.contains(s)) {
                            l.add(s);
                        }
                        ts.add(trans);
                        for (EstadoTA eaux : transitar(ehijo)) {
                            q.add(new EstadoTA[] { ehijo, eaux });
                        }
                    }
                }
            }
        }
        return l;
    }

    public List<EstadoTA> backtracking(EstadoTA estado) {
        TreeMap<String, Set<String>> propietarias = copiaPropietarias(estado.propietarias);
        ArrayList<EstadoTA> laux = new ArrayList<EstadoTA>();
        TreeSet<String> conjIni = new TreeSet<String>();
        for (int i = 0; i < actividadesOrdenadas.size(); i++) {
            conjIni.add(actividadesOrdenadas.get(i));
        }
        backtracking2(estado, conjIni, new TreeSet<String>(), propietarias, laux);
        return laux;
    }

    private boolean actividadEjecutada(String a, EstadoTA estadoini) {
        if (estadoini.actividades.getEstado(a).equals(EstadoActividad.Executing)) {
            Set<Actividad> setaux = activGen.getItem(a).getActividadesHijas();
            if (!setaux.isEmpty()) {
                boolean finalizada = true;
                Iterator<Actividad> it = setaux.iterator();
                while (finalizada && it.hasNext()) {
                    Actividad act = it.next();
                    finalizada = estadoini.actividades.getEstado(act.getNombre()).equals(EstadoActividad.Finalized);
                }
                return finalizada;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private void backtracking2(EstadoTA eini, TreeSet<String> conjIni, TreeSet<String> conjHechos, TreeMap<String, Set<String>> propietarias, ArrayList<EstadoTA> laux) {
        boolean algunaLanza = false;
        for (Iterator<String> it = conjIni.iterator(); it.hasNext(); ) {
            String act = it.next();
            if (!conjHechos.contains(act)) {
                TreeSet<String> propaux = new TreeSet<String>();
                Item[] itemsNecesarios = activGen.getItem(act).getItemNecesarios();
                for (int i = 0; i < itemsNecesarios.length; i++) {
                    propaux.add(itemsNecesarios[i].getClave());
                }
                Actividad padre = activGen.getItem(act).getPadre();
                boolean padreActivo = false;
                if ((padre == null) || (eini.getEstadoActividad(padre.getNombre()).equals(EstadoActividad.Executing))) {
                    padreActivo = true;
                }
                if (padreActivo && (eini.actividades.getEstado(act).equals(EstadoActividad.Waiting)) && puedeUsarlos(act, propaux, eini) && activGen.getItem(act).CondicionesSatisfy(eini)) {
                    propietarias.put(act, propaux);
                    TreeSet<String> conjIni2 = new TreeSet<String>(conjIni);
                    conjIni2.remove(act);
                    Set<String> conjCompat = tabla.get(act);
                    if (conjCompat != null) {
                        conjIni2.retainAll(tabla.get(act));
                        if (conjIni2.size() == 0) {
                            EstadoTA estadoaux = new EstadoTA(eini);
                            estadoaux.propietarias = copiaPropietarias(propietarias);
                            estadoaux.lanzarPosibles(this);
                            laux.add(estadoaux);
                        } else {
                            TreeSet<String> conjHechos2 = new TreeSet<String>(conjHechos);
                            conjHechos2.add(act);
                            backtracking2(eini, conjIni2, conjHechos2, propietarias, laux);
                        }
                    } else {
                        EstadoTA estadoaux = new EstadoTA(eini);
                        estadoaux.propietarias = copiaPropietarias(propietarias);
                        estadoaux.lanzarPosibles(this);
                        laux.add(estadoaux);
                    }
                    propietarias.remove(act);
                    algunaLanza = true;
                }
            }
        }
        if (!algunaLanza) {
            EstadoTA estadoaux = new EstadoTA(eini);
            estadoaux.propietarias = copiaPropietarias(propietarias);
            estadoaux.lanzarPosibles(this);
            laux.add(estadoaux);
        }
    }

    private TreeMap<String, Set<String>> copiaPropietarias(TreeMap<String, Set<String>> propietarias) {
        TreeMap<String, Set<String>> propaux = new TreeMap<String, Set<String>>();
        for (Iterator<String> it = propietarias.keySet().iterator(); it.hasNext(); ) {
            String a = it.next();
            propaux.put(a, new TreeSet<String>(propietarias.get(a)));
        }
        return propaux;
    }

    public String[] getItemsNombre() {
        return items;
    }

    public String[] getActividadesNombre() {
        return actividades;
    }

    public String[] getActividadesHijas(String actividad) {
        TreeSet<String> laux = new TreeSet<String>();
        for (Iterator<Actividad> it = this.activGen.getItem(actividad).getActividadesHijas().iterator(); it.hasNext(); ) {
            Actividad a = it.next();
            laux.add(a.getNombre());
        }
        return laux.toArray(new String[0]);
    }

    private String nombreTransicion(EstadoTA eini, EstadoTA efin) {
        StringBuffer strbuf = new StringBuffer();
        StringBuffer strbuf2 = new StringBuffer();
        for (int a = 0; a < actividades.length; a++) {
            String s = actividades[a];
            if (!eini.getEstadoActividad(s).equals(efin.getEstadoActividad(s))) {
                strbuf.append(s + "->" + efin.getEstadoActividad(s).toString() + ", ");
            }
            if ((eini.getItemsPoseidos(s) == null) || (efin.getItemsPoseidos(s) == null) || (!Arrays.equals(eini.getItemsPoseidos(s), efin.getItemsPoseidos(s)))) {
                String[] itemsp = efin.getItemsPoseidos(s);
                if (itemsp != null) {
                    java.util.Arrays.sort(itemsp);
                    strbuf2.append(s + " posee ");
                    for (int i = 0; i < itemsp.length; i++) {
                        strbuf2.append(itemsp[i] + ", ");
                    }
                } else {
                    itemsp = eini.getItemsPoseidos(s);
                    if (itemsp != null) {
                        java.util.Arrays.sort(itemsp);
                        strbuf2.append(s + " suelta ");
                        for (int i = 0; i < itemsp.length; i++) {
                            strbuf2.append(itemsp[i] + ", ");
                        }
                    }
                }
            }
        }
        if ((strbuf.length() == 0) && (strbuf2.length() == 0)) {
            return efin.toString();
        } else {
            return strbuf.append(strbuf2).toString();
        }
    }

    private boolean puedeUsarlos(String a, TreeSet<String> propaux, EstadoTA eini) {
        boolean puede = true;
        for (Iterator<String> it = propaux.iterator(); puede && it.hasNext(); ) {
            String item = it.next();
            if (eini.items.getEstado(item).equals(EstadoItem.BUSY)) {
                puede = itemHeredado(a, item, eini);
            } else if (eini.items.getEstado(item).equals(EstadoItem.DISPOSED)) {
                puede = false;
            }
        }
        return puede;
    }

    private boolean itemHeredado(String a, String item, EstadoTA estadoini) {
        Set<String> antecesores = activGen.getItem(a).getAntecesores();
        if (!antecesores.isEmpty()) {
            Iterator<String> it = antecesores.iterator();
            boolean unica = true;
            while (unica && it.hasNext()) {
                String s = it.next();
                if (estadoini.propietarias.containsKey(s)) {
                    unica = !estadoini.propietarias.get(s).contains(item);
                }
            }
            return !unica;
        } else {
            return false;
        }
    }

    public LinkedList<String> getActividadesOrdenadas() {
        return actividadesOrdenadas;
    }

    public void loadModel(Modelo<EstadoTA> model) {
        model.visita(this);
    }

    public void loadFromGenerators() {
        actividades = activGen.getConjunto().keySet().toArray(new String[0]);
        items = itemGen.getItems();
        actividadesOrdenadas = new LinkedList<String>();
        for (int i = 0; i < actividades.length; i++) {
            Actividad a = activGen.getItem(actividades[i]);
            if (a.getPadre() == null) {
                actividadesOrdenadas.add(a.getNombre());
            }
        }
        int a = 0;
        while (actividadesOrdenadas.size() < actividades.length) {
            Actividad act = activGen.getItem(actividadesOrdenadas.get(a));
            Set<Actividad> setaux = act.getActividadesHijas();
            if (!setaux.isEmpty()) {
                for (Iterator<Actividad> it = setaux.iterator(); it.hasNext(); ) {
                    actividadesOrdenadas.addLast(it.next().getNombre());
                }
            }
            a++;
        }
        int numact = actividadesOrdenadas.size();
        tabla = new TreeMap<String, Set<String>>();
        for (int i = 0; i < numact; i++) {
            Actividad a1 = activGen.getItem(actividadesOrdenadas.get(i));
            Item[] s1 = a1.getItemNecesarios();
            TreeSet<String> conjComp = new TreeSet<String>();
            for (int j = 0; j < numact; j++) {
                if (i != j) {
                    Actividad a2 = activGen.getItem(actividadesOrdenadas.get(j));
                    if (!a1.parienteDe(a2)) {
                        Item[] s2 = a2.getItemNecesarios();
                        boolean incompatibles = false;
                        for (int k = 0; !incompatibles && k < s1.length; k++) {
                            Item e1 = s1[k];
                            for (int m = 0; !incompatibles && m < s2.length; m++) {
                                Item e2 = s2[m];
                                if (e1.compareTo(e2) == 0) {
                                    incompatibles = true;
                                }
                            }
                        }
                        if (!incompatibles) {
                            conjComp.add(a2.getNombre());
                        }
                    } else {
                        conjComp.add(a2.getNombre());
                    }
                }
            }
            tabla.put(a1.getNombre(), conjComp);
        }
    }
}
