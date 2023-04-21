package casosDePrueba;

import analizador.*;

class ComunicadorSimulador implements Comunicable {

    byte[] ultimaTramaRecibida;

    private boolean conectado = false;

    private native int iniciar();

    private native void enviar(byte comando);

    private native String recibir();

    private native void finalizar();

    static {
        System.out.println("Buscando librerias en:" + System.getProperty("java.library.path"));
        try {
            System.load("/usr/lib/comunicado.so");
            System.out.println("Librería de Linux cargada correctamente.");
        } catch (java.lang.UnsatisfiedLinkError e) {
            System.out.println("Error al cargar la librería en Linux.");
            e.printStackTrace();
            try {
                System.loadLibrary("Comunicado");
                System.out.println("Librería de Windows cargada correctamente.");
            } catch (Exception b) {
                System.out.println("Error al cargar la librería en Windows.");
                b.printStackTrace();
            }
        }
    }

    private ComunicadorSimulador() {
        int fd = this.iniciar();
        if (fd == -1) {
            System.out.println("No se ha podido abrir el puerto serie.");
            conectado = false;
        } else {
            conectado = true;
        }
    }

    public static Comunicable newComunicador() {
        return new ComunicadorSimulador();
    }

    public void enviarComando(String comando) {
        int i;
        byte a;
        if (conectado) {
            System.out.println("Enviando comando: '" + comando + "'...");
            this.enviar((byte) '\n');
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            for (i = 0; i < comando.length(); i++) {
                a = (byte) comando.charAt(i);
                this.enviar(a);
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            this.enviar((byte) '\r');
        } else {
            System.out.println("No se ha establecido conexión aún.");
        }
    }

    public String recibirComando() {
        String retorno;
        if (conectado) {
            System.out.println("Recibiendo...");
            retorno = this.recibir();
            ultimaTramaRecibida = retorno.getBytes();
        } else {
            throw new NullPointerException("Hardware externo no conectado...");
        }
        return retorno;
    }

    public String obtenerUltimaTrama() {
        String retorno = new String(ultimaTramaRecibida);
        return retorno;
    }

    public void cerrarSerie() {
        this.finalizar();
    }

    public void guardarMuestras() {
    }

    public void abrirMuestras() {
    }

    public void crearJFileChooser() {
    }
}
