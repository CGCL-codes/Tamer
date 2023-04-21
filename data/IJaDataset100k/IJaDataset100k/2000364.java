package arduino;

public class Fake implements Arduino {

    int n_sensores_t;

    public byte sensores_t[][];

    boolean regando;

    int n_alarmas;

    long[] alarmas = new long[56];

    public boolean startReg() {
        regando = true;
        return true;
    }

    public boolean stopReg() {
        regando = false;
        return false;
    }

    public boolean comprobarReg() {
        return regando;
    }

    public int contarSensoresT() {
        this.n_sensores_t = 2;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2;
    }

    public byte[][] listarSensoresT() {
        this.contarSensoresT();
        this.sensores_t = new byte[this.n_sensores_t][8];
        this.sensores_t[0][0] = new Byte("00").byteValue();
        this.sensores_t[0][1] = new Byte("00").byteValue();
        this.sensores_t[0][2] = new Byte("00").byteValue();
        this.sensores_t[0][3] = new Byte("00").byteValue();
        this.sensores_t[0][4] = new Byte("00").byteValue();
        this.sensores_t[0][5] = new Byte("00").byteValue();
        this.sensores_t[0][6] = new Byte("00").byteValue();
        this.sensores_t[0][7] = new Byte("00").byteValue();
        this.sensores_t[1][0] = new Byte("11").byteValue();
        this.sensores_t[1][1] = new Byte("11").byteValue();
        this.sensores_t[1][2] = new Byte("11").byteValue();
        this.sensores_t[1][3] = new Byte("11").byteValue();
        this.sensores_t[1][4] = new Byte("11").byteValue();
        this.sensores_t[1][5] = new Byte("11").byteValue();
        this.sensores_t[1][6] = new Byte("11").byteValue();
        this.sensores_t[1][7] = new Byte("11").byteValue();
        return this.sensores_t;
    }

    public Float obtenerTemperatura(byte[] sensor) {
        if (sensor == this.sensores_t[1]) {
            return new Float("34.2");
        } else {
            return new Float("24.2");
        }
    }

    public Long obtenerPresionBMP085() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Long(101325);
    }

    public Float obtenerTemperaturaBMP085() {
        return new Float(30);
    }

    public Float obtenerAlturaBMP085() {
        return new Float(0);
    }

    public Float obtenerHumedadHH10D() {
        return new Float(45.5);
    }

    public int obtenerHumedadSuelo() {
        return 50;
    }

    public int initialize() {
        return 0;
    }

    public void close() {
    }

    public boolean establecerHora(Long tiempoUnix) {
        return true;
    }

    public int establecerAlarmaOn(Long tiempoUnix) {
        this.alarmas[n_alarmas] = tiempoUnix;
        return n_alarmas++;
    }

    public int establecerAlarmaOff(Long tiempoUnix) {
        this.alarmas[n_alarmas] = tiempoUnix;
        return n_alarmas++;
    }

    public int establecerAlarmaRepOn(int horas, int minutos, int segundos) {
        this.alarmas[n_alarmas] = horas * 24 + minutos * 60 + segundos;
        return n_alarmas++;
    }

    public int establecerAlarmaRepOff(int horas, int minutos, int segundos) {
        this.alarmas[n_alarmas] = horas * 24 + minutos * 60 + segundos;
        return n_alarmas++;
    }

    public boolean eliminarAlarma(int alarmaId) {
        n_alarmas++;
        return true;
    }

    public boolean eliminarAlarmas() {
        n_alarmas = 0;
        return false;
    }
}
