package herramientas;

import java.util.ArrayList;
import java.util.Iterator;
import jess.Filter;
import jess.JessException;
import jess.Rete;
import modelo.Movil;

/**Clase utilizada para llamar a las funciones de Jess.
 * 
 * @author DaNieLooP
 *
 */
public class JessWrapper {

    /**
	 * RETE
	 */
    private Rete engine;

    /**
	 * Objeto InfoParser para parsear la informaci�n de internet.
	 */
    private InfoParser parser;

    private ArrayList listaMoviles;

    private ArrayList listaDef;

    /**
	 * Constructor de la clase, donde se inicializan las variables.
	 */
    public JessWrapper() {
        listaDef = new ArrayList();
        engine = new Rete();
        parser = new InfoParser();
        listaMoviles = new ArrayList();
        String appPath = System.getProperties().getProperty("user.dir");
        parser.leeFichero(appPath + "/moviles.txt");
        listaMoviles = parser.getMoviles();
        try {
            engine.reset();
            engine.addAll(listaMoviles);
            engine.batch("reglas/segmentos.clp");
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**
	 * Vac�a la lista de m�viles al hacer la consulta.
	 */
    public void flushListaDef() {
        listaDef.clear();
    }

    /**
	 * @return the listaDef
	 */
    public ArrayList getListaDef() {
        return listaDef;
    }

    /**
	 * Limpia el RETE de hechos y reglas.
	 */
    public void inicializaRete() {
        try {
            engine.clear();
            engine.reset();
            engine.addAll(listaMoviles);
            engine.batch("reglas/segmentos.clp");
            engine.run();
        } catch (JessException ex) {
            System.out.println("JessException: " + ex);
        }
    }

    /**
	 * Corre el engine y recupera los hechos en forma de objetos de la clase M�vil.
	 */
    public void run() {
        Iterator i;
        ArrayList temp = new ArrayList();
        try {
            engine.run();
            i = engine.getObjects(new Filter.ByClass(Movil.class));
            while (i.hasNext()) {
                Movil t = (Movil) i.next();
                temp.add(t);
            }
            if (temp.size() > 0) {
                listaDef = temp;
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        } catch (Exception e) {
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void s3g(int i) {
        try {
            if (i == 1) {
                engine.batch("reglas/selecciona3G.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaNo3G.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sBT(int i) {
        try {
            if (i == 1) {
                engine.batch("../reglas/seleccionaBT.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("../reglas/seleccionaNoBT.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sCamara(int i) {
        try {
            if (i == 1) {
                engine.batch("../reglas/seleccionaNoCamara.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaBajoCamara.clp");
                this.run();
            } else if (i == 3) {
                engine.batch("reglas/seleccionaMedioCamara.clp");
                this.run();
            } else if (i == 4) {
                engine.batch("reglas/seleccionaAltoCamara.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sCSlot(int i) {
        try {
            if (i == 1) {
                engine.batch("reglas/seleccionaCSlot.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaNoCSlot.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sDesign(int i) {
        try {
            if (i == 1) {
                engine.batch("reglas/seleccionaMuyBajoDesign.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaBajoDesign.clp");
                this.run();
            } else if (i == 3) {
                engine.batch("reglas/seleccionaMedioDesign.clp");
                this.run();
            } else if (i == 4) {
                engine.batch("reglas/SeleccionaAltoDesign.clp");
                this.run();
            } else if (i == 5) {
                engine.batch("reglas/seleccionaMuyAltoDesign.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**
	 * Muestra por pantalla los hechos existentes en la memoria de trabajo.
	 */
    public void showFacts() {
        try {
            engine.eval("(facts)");
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sPeso(int i) {
        try {
            if (i == 1) {
                engine.batch("reglas/seleccionaBajoPeso.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaMedioPeso.clp");
                this.run();
            } else if (i == 3) {
                engine.batch("reglas/seleccionaAltoPeso.clp");
                this.run();
            } else if (i == 4) {
                engine.batch("reglas/seleccionaMuyAltoPeso.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sPrecio(int i) {
        try {
            if (i == 1) {
                engine.batch("reglas/seleccionaMuyBajoPrecio.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaBajoPrecio.clp");
                this.run();
            } else if (i == 3) {
                engine.batch("reglas/seleccionaMedioPrecio.clp");
                this.run();
            } else if (i == 4) {
                engine.batch("reglas/seleccionaAltoPrecio.clp");
                this.run();
            } else if (i == 5) {
                engine.batch("reglas/seleccionaMuyAltoPrecio.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sRend(int i) {
        try {
            if (i == 1) {
                engine.batch("reglas/seleccionaMuyBajoRend.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaBajoRend.clp");
                this.run();
            } else if (i == 3) {
                engine.batch("reglas/seleccionaMedioRend.clp");
                this.run();
            } else if (i == 4) {
                engine.batch("reglas/seleccionaAltoRend.clp");
                this.run();
            } else if (i == 5) {
                engine.batch("reglas/seleccionaMuyAltoRend.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sVibr(int i) {
        try {
            if (i == 1) {
                engine.batch("reglas/seleccionaVibr.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaNoVibr.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sVolumen(int i) {
        try {
            if (i == 1) {
                engine.batch("reglas/seleccionaBajoVolumen.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaMedioVolumen.clp");
                this.run();
            } else if (i == 3) {
                engine.batch("reglas/seleccionaAltoVolumen.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }

    /**Selecciona reglas en funci�n de los par�metros del GUI:
	 * 
	 * @param i Par�metro para seleccionar la regla.
	 */
    public void sWLan(int i) {
        try {
            if (i == 1) {
                engine.batch("reglas/seleccionaWLan.clp");
                this.run();
            } else if (i == 2) {
                engine.batch("reglas/seleccionaNoWLan.clp");
                this.run();
            }
        } catch (JessException e) {
            System.out.println("JessException: " + e);
        }
    }
}
