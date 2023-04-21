package packbridge;

/**
 *
 * @author jose
 */
public class Generacion {

    public int poblacion = 10;

    public Puente individuos[];

    public Generacion(int p) {
        poblacion = p;
        individuos = new Puente[poblacion];
        for (int i = 0; i < poblacion; i++) {
            individuos[i] = new Puente();
        }
    }

    public void cruzarymutar() {
        int total_fitness = individuos[0].fitness;
        for (int i = 1; i < poblacion; i++) {
            total_fitness = total_fitness + individuos[i].fitness;
        }
        for (int i = 1; i < individuos.length; i++) {
            Puente aux = individuos[i];
            int j;
            for (j = i - 1; j >= 0 && individuos[j].fitness < aux.fitness; j--) individuos[j + 1] = individuos[j];
            individuos[j + 1] = aux;
        }
        int buenos = 0;
        while (individuos[buenos].fitness >= (total_fitness / poblacion)) {
            buenos++;
        }
        System.out.println("Fitness ordenados");
        System.out.println(buenos);
        int k = 4;
        Puente individuos_aux[] = new Puente[poblacion];
        for (int i = 0; i < poblacion; i++) {
            double p_reproduccion = ((double) individuos[i].fitness / (double) total_fitness) * 100 * (poblacion / 2);
            if (i <= 1) {
                p_reproduccion = 100;
                individuos_aux[i] = individuos[i];
                individuos_aux[i + 2] = individuos[i].cruzar(individuos[i]);
            }
            System.out.print("Fitness " + i + ": " + individuos[i].fitness + " P.Reproduccion: " + p_reproduccion);
            if ((p_reproduccion + Math.random() * 100) > 100) {
                int cualquiera = (int) (Math.random() * buenos);
                Puente aux = individuos[cualquiera];
                System.out.print(" Cruce con: " + cualquiera + " ");
                Puente hijo = individuos[i].cruzar(aux);
                if (k < poblacion) {
                    individuos_aux[k] = hijo;
                    k++;
                }
            }
            System.out.println("");
        }
        int iaux = 0;
        for (; k < poblacion - 1; k++) {
            int cualquiera = (int) (Math.random() * buenos);
            Puente aux = individuos[cualquiera];
            Puente hijo = individuos[iaux].cruzar(aux);
            individuos_aux[k] = hijo;
            iaux++;
        }
        individuos_aux[poblacion - 1] = individuos[0].cruzar(new Puente());
        System.arraycopy(individuos_aux, 0, individuos, 0, poblacion);
    }
}
