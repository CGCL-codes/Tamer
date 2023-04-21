package librodeesher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class LeerProfesion {

    private Esher esher;

    private boolean creandoPJ = true;

    LeerProfesion(Esher tmp_esher) {
        try {
            esher = tmp_esher;
            LeerFicheroProfesion();
        } catch (Exception ex) {
            Logger.getLogger(LeerProfesion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    LeerProfesion(Esher tmp_esher, boolean tmp_creandoPJ) {
        creandoPJ = tmp_creandoPJ;
        try {
            esher = tmp_esher;
            LeerFicheroProfesion();
        } catch (Exception ex) {
            Logger.getLogger(LeerProfesion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void LeerFicheroProfesion() throws Exception {
        int lineaLeida = 2;
        LimpiarAntiguaProfesion();
        String ficheroProfesion = esher.BuscarDirectorioModulo(esher.directorioRolemaster.DIRECTORIO_PROFESION + File.separator + esher.pj.profesion + ".txt");
        List<String> lines = esher.directorioRolemaster.LeerLineasProfesion(ficheroProfesion);
        lineaLeida = AsignarCaracteristicasBasicas(lines, lineaLeida);
        lineaLeida = AsignarReinosDisponiblesPorProfesión(lines, lineaLeida);
        lineaLeida = AsignarBonificacionPorProfesion(lines, lineaLeida);
        lineaLeida = AsignarCostesCategorias(lines, lineaLeida);
        lineaLeida = AsignarHabilidadesComunes(lines, lineaLeida);
        lineaLeida = AsignarHabilidadesProfesionales(lines, lineaLeida);
        lineaLeida = AsignarHabilidadesRestringidas(lines, lineaLeida);
        Magia magia = new Magia(esher);
        magia.PrepararCostesListas();
        lineaLeida = magia.AsignarCostesHechizos(lines, lineaLeida);
        lineaLeida = AsignarCostesAdiestramiento(lines, lineaLeida);
    }

    /**
     * Asigna todos los bonuses a las categorias típicas de la profesión escogida.
     */
    private int AsignarBonificacionPorProfesion(List<String> lines, int index) {
        index += 4;
        while (!lines.get(index).equals("")) {
            String lineaCategoriaProfesional = lines.get(index);
            String[] vectorCategoriaProfesional = lineaCategoriaProfesional.split("\t");
            String nombre = vectorCategoriaProfesional[0];
            int bonus = Integer.parseInt(vectorCategoriaProfesional[1]);
            Categoria categoriaProfesional = esher.pj.DevolverCategoriaDeNombre(nombre);
            categoriaProfesional.bonusProfesion = bonus;
            index++;
        }
        return index;
    }

    private int AsignarReinosDisponiblesPorProfesión(List<String> lines, int index) {
        index += 4;
        esher.pj.reinosDeProfesion = new ArrayList<String>();
        try {
            while (!lines.get(index).equals("")) {
                String lineaReino = lines.get(index);
                esher.pj.reinosDeProfesion.add(lineaReino);
                index++;
            }
        } catch (IndexOutOfBoundsException iob) {
        }
        return index;
    }

    private int AsignarCaracteristicasBasicas(List<String> lines, int index) {
        if (esher.pj.nivel == 1 && creandoPJ) {
            String preferenciasCaracteristicas = lines.get(index);
            if (!lines.get(index).equals("Indiferente")) {
                esher.pj.arrayCaracteristicasProfesion = preferenciasCaracteristicas.split(" ");
                if (esher.pj.ObtenerPuntosCaracteristicasGastados() < 660 || !(esher.pj.caracteristicas.DevolverCaracteristicaDeAbreviatura(esher.pj.arrayCaracteristicasProfesion[0]).ObtenerPuntosTemporal() >= 90) && !(esher.pj.caracteristicas.DevolverCaracteristicaDeAbreviatura(esher.pj.arrayCaracteristicasProfesion[1]).ObtenerPuntosTemporal() >= 90)) {
                    for (int i = 0; i < esher.pj.arrayCaracteristicasProfesion.length; i++) {
                        String car = esher.pj.arrayCaracteristicasProfesion[i];
                        try {
                            Caracteristica caract = esher.pj.caracteristicas.DevolverCaracteristicaDeAbreviatura(car);
                            caract.CrearPuntosTemporal(esher.baseCaracteristicas[i]);
                        } catch (NullPointerException npe) {
                            System.out.println("Caracteristica " + car + " mostrada en el archivo " + esher.pj.profesion + ".txt no existente.");
                        }
                    }
                }
            } else {
                if (esher.pj.ObtenerPuntosCaracteristicasGastados() < 660) {
                    List<Integer> listaEnteros = esher.ObtenerListaAleatoriaDeEnteros(10);
                    int i = 0;
                    Caracteristica car = null;
                    try {
                        for (int j = 0; j < esher.pj.caracteristicas.Size(); j++) {
                            car = esher.pj.caracteristicas.Get(j);
                            car.CrearPuntosTemporal(esher.baseCaracteristicas[listaEnteros.get(i)]);
                            i++;
                        }
                    } catch (NullPointerException npe) {
                        System.out.println("Fallo al intentar asignar las caracteristicas de profesion de forma aleatoria.");
                    }
                }
            }
        }
        return index++;
    }

    private int AsignarCostesCategorias(List<String> lines, int index) {
        index += 3;
        esher.pj.costearmas = new CosteArmas(esher);
        esher.pj.armas.ResetearCuentaArmas();
        while (!lines.get(index).equals("")) {
            String lineaCategoria = lines.get(index);
            String[] vectorCategoria = lineaCategoria.split("\t");
            String nombre = vectorCategoria[0];
            if (nombre.startsWith("Armas·")) {
                esher.pj.costearmas.AñadirCosteRango(esher.pj.ConvertirStringCosteEnIntCoste(vectorCategoria[1]));
                nombre = "Armas·" + esher.pj.armas.ObtenerSiguienteArmaPreferida();
            } else {
                Categoria categoriaProfesional = esher.pj.DevolverCategoriaDeNombre(nombre);
                try {
                    categoriaProfesional.CambiarCosteRango(esher.pj.ConvertirStringCosteEnIntCoste(vectorCategoria[1]));
                } catch (NullPointerException npe) {
                    System.out.println("Categoría desconocida: " + nombre);
                } catch (ArrayIndexOutOfBoundsException aiob) {
                    System.out.println("Categoría mal definida: " + nombre);
                }
            }
            index++;
        }
        return index;
    }

    private int AsignarHabilidadesEspeciales(List<String> lines, int index, String tipo) {
        index += 3;
        Habilidad hab;
        while (!lines.get(index).equals("")) {
            String lineaHabilidad = lines.get(index);
            String[] vectorHabilidades = lineaHabilidad.split(", ");
            for (int i = 0; i < vectorHabilidades.length; i++) {
                if (vectorHabilidades[i].startsWith("{") && creandoPJ) {
                    vectorHabilidades[i] = esher.pj.SeleccionarNombreHabilidadAleatoriaDeListado(vectorHabilidades[i].replace("}", "").replace("{", ""), "Profesional", "profesion");
                }
                if (!vectorHabilidades[i].equals("Ninguna")) {
                    try {
                        hab = esher.pj.DevolverHabilidadDeNombre(vectorHabilidades[i]);
                        if (tipo.equals("Común")) {
                            hab.HacerComunProfesion();
                        }
                        if (tipo.equals("Profesional")) {
                            hab.HacerProfesional();
                        }
                        if (tipo.equals("Restringida")) {
                            hab.HacerRestringida();
                        }
                    } catch (NullPointerException npe) {
                        if (creandoPJ) {
                            if (!esher.pj.SeleccionarGrupoHabilidadesEspeciales(tipo, vectorHabilidades[i], "profesion")) {
                                System.out.println("Habilidad desconocida: " + vectorHabilidades[i]);
                            }
                        }
                    }
                }
            }
            index++;
        }
        return index;
    }

    private int AsignarHabilidadesComunes(List<String> lines, int index) throws Exception {
        return AsignarHabilidadesEspeciales(lines, index, "Común");
    }

    private int AsignarHabilidadesProfesionales(List<String> lines, int index) throws Exception {
        return AsignarHabilidadesEspeciales(lines, index, "Profesional");
    }

    private int AsignarHabilidadesRestringidas(List<String> lines, int index) throws Exception {
        return AsignarHabilidadesEspeciales(lines, index, "Restringida");
    }

    private int AsignarCostesAdiestramiento(List<String> lines, int index) {
        index += 3;
        esher.pj.BorraAntiguosCostesAdiestramiento();
        while (!lines.get(index).equals("")) {
            while (!lines.get(index).equals("")) {
                String lineaAdiestramiento = lines.get(index);
                String[] vectorAdiestramiento = lineaAdiestramiento.split("\t");
                if (vectorAdiestramiento[1].contains("+")) {
                    esher.pj.costesAdiestramientos.AñadirAdiestramientoPreferido(vectorAdiestramiento[0], Integer.parseInt(vectorAdiestramiento[1].replace("+", "")));
                } else {
                    if (vectorAdiestramiento[1].contains("-")) {
                        esher.pj.costesAdiestramientos.AñadirAdiestramientoProhibido(vectorAdiestramiento[0], Integer.parseInt(vectorAdiestramiento[1].replaceAll("-", "")));
                    } else {
                        esher.pj.costesAdiestramientos.AñadirAdiestramiento(vectorAdiestramiento[0], Integer.parseInt(vectorAdiestramiento[1]));
                    }
                }
                index++;
            }
        }
        return index;
    }

    private void LimpiarAntiguaProfesion() {
        for (int i = 0; i < esher.pj.categorias.size(); i++) {
            Categoria cat = esher.pj.categorias.get(i);
            cat.bonusProfesion = 0;
            for (int j = 0; j < cat.listaHabilidades.size(); j++) {
                Habilidad hab = cat.listaHabilidades.get(j);
                hab.NoEsComunProfesion();
            }
        }
    }
}
