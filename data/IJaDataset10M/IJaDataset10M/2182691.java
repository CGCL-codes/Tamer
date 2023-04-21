package ModuloDiagnosticador;

import ModuloBaseDelConocimiento.Caso;
import ModuloBaseDelConocimiento.ListaDeSintomas;
import ModuloBaseDelConocimiento.SintomaOSigno;
import ModuloControladorBD.CasoJpaController;
import ModuloControladorBD.ListaDeSintomasJpaController;
import ModuloControladorBD.SintomaOSignoJpaController;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * Esta clase contiene los metodos de la red neuronal artificial Kohonen + ART(adaptative Reasonance Theory)
 * @author rayner
 */
public class ArtKohonen {

    private int numeroDeNeuronas;

    private int numeroDeEntradas;

    private ArrayList<NeuronaKohonen> neuronas;

    private List<Caso> casos;

    private double aprendizaje;

    private double similitud;

    private int winner;

    private double distWinner;

    private HashMap mapeadorSintomasID;

    private final double INF = (double) (1 << 30);

    /**
     * Constructor de la clase;
     * @param apren Es la taza de aprendizaje de la red
     * @param simil Es el porcentaje de similitud por defecto(funciona como el umbral)
     * @param cas Contiene los casos con los cuales sera entrenada la red.
     */
    public ArtKohonen(double apren, double simil) {
        CasoJpaController cas_con = new CasoJpaController();
        mapeadorSintomasID = new HashMap();
        casos = cas_con.findCasoEntities();
        numeroDeNeuronas = 0;
        aprendizaje = apren;
        similitud = simil;
        neuronas = new ArrayList<NeuronaKohonen>();
        initMaping();
    }

    private void initMaping() {
        ListaDeSintomasJpaController controlador_sos = new ListaDeSintomasJpaController();
        List<ListaDeSintomas> sintomasUnitarios = controlador_sos.findListaDeSintomasEntities();
        Iterator it = sintomasUnitarios.iterator();
        numeroDeEntradas = 0;
        while (it.hasNext()) {
            ListaDeSintomas sin = (ListaDeSintomas) it.next();
            mapeadorSintomasID.put(sin.getNombre(), (Object) numeroDeEntradas++);
        }
    }

    private double[] transSintToInput(int id) {
        List<SintomaOSigno> sintomas = casos.get(id).getSintomaOSignoList();
        double casoNumerico[] = new double[numeroDeEntradas];
        double val = 0;
        for (int i = 0; i < sintomas.size(); i++) {
            if (sintomas.get(i).getPresente()) val = 1.0; else val = -1.0;
            casoNumerico[(Integer) mapeadorSintomasID.get(sintomas.get(i).getNombre())] = val;
        }
        return casoNumerico;
    }

    private void calculateWinner(double[] casoNumerico) {
        winner = -1;
        distWinner = INF;
        double distCandidato;
        for (int i = 0; i < numeroDeNeuronas; i++) {
            distCandidato = neuronas.get(i).calcularDistEuclidiana(casoNumerico);
            if (distCandidato < distWinner) {
                winner = i;
                distWinner = distCandidato;
            }
        }
    }

    private double normalizar() {
        double norm = 0.0;
        double zeros[] = new double[numeroDeEntradas];
        double eu = neuronas.get(winner).calcularDistEuclidiana(zeros);
        norm = (eu - distWinner) / eu;
        return norm;
    }

    private void crearNuevaNeurona(double inp[]) {
        NeuronaKohonen new_neu = new NeuronaKohonen(neuronas.size(), inp, numeroDeEntradas);
        numeroDeNeuronas++;
        neuronas.add(new_neu);
    }

    private void modificarPesosNeurona(double inp[], int targetNeuronaLVQ, String desp, boolean divi) {
        neuronas.get(winner).updatePesos(numeroDeNeuronas, inp, targetNeuronaLVQ, desp, divi);
    }

    public void EntrenarRed() {
        int m = casos.size();
        int iteraciones = 30;
        for (int i = 0; i < m * iteraciones; i++) {
            double inp[] = transSintToInput(i % m);
            calculateWinner(inp);
            double umbral = 0;
            if (winner != -1) umbral = normalizar();
            if (umbral < similitud) {
                crearNuevaNeurona(inp);
            } else {
                boolean divi = (i != 0 && ((i / m) != ((i - 1) / m))) ? true : false;
                modificarPesosNeurona(inp, casos.get(i % m).getEnfermedad().getId(), casos.get(i % m).getDescripcion(), divi);
            }
        }
    }

    public Caso Recuperar(double[] nums) {
        calculateWinner(nums);
        double umbral = normalizar();
        if (umbral < similitud) {
            JOptionPane.showMessageDialog(null, "Cuidado, distancia muy larga");
        }
        return neuronas.get(winner).recuperarLVQ(nums);
    }
}
