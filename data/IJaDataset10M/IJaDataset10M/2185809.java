package es.usc.citius.servando.android.medim.Storage;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import es.usc.citius.servando.android.models.MIT.MITSignalSpecification;

/**
 * Canal de datos. Funciona a modo de buffer circular
 * 
 * @author Ángel Piñeiro
 */
public final class Channel {

    Logger log = Logger.getLogger(Channel.class);

    /**
	 * Nome do canal de datos
	 */
    private String name;

    /**
	 * Vector de shorts oara as mostras
	 */
    public short[] data;

    /**
	 * Numero de bits de resolucion de la muestra
	 */
    private short resolution;

    /**
	 * Numero de muestras por segundo.
	 */
    private short frequency;

    /**
	 * Punteiro de escritura
	 */
    private int in = 0;

    /**
	 * Punteiro de lectura
	 */
    private int out = 0;

    /**
	 * Capacidade do buffer
	 */
    private int capacity;

    /**
	 * Contador de mostras almacenadas. Increméntase ao facer write, e decreméntase ao facer read, de xeito que sempre
	 * indica as mostras que hai no vector de datos {@link #data}.
	 * 
	 * Utilízase para distinguir se o buffer está baleiro ou cheo, xa que só coa posición dos punteiros non é posible.
	 * Increméntase cada vez que se escribe unha mostra no canal e decreméntase cada vez que se quita.
	 */
    private int fillCount = 0;

    /**
	 * Crea un novo canal de datos para gardar e ler mostras.
	 * 
	 * @param resolution
	 *            Resolución do canal en bits/mostra
	 * @param frecuency
	 *            Frecuencia de mostraxe en mostras/segundo
	 * @param name
	 *            Nome do canal (normalmente o nome da derivación correspondente)
	 * @param capacityInSeconds
	 *            Capacidade do canal en segundos. A capacidade real en mostras calcúlase internamente multiplicando
	 *            este valor pola frecuencia de mostraxe.
	 */
    public Channel(short resolution, short frequency, String name, int capacityInSeconds) {
        this.resolution = resolution;
        this.frequency = frequency;
        this.name = name;
        this.capacity = frequency * capacityInSeconds;
        this.data = new short[this.capacity];
    }

    /**
	 * Engade un vector de muestras ao canal.
	 * 
	 * @param samples
	 *            vector de mostras a engadir ao canal
	 * @throws #{@link BufferOverflowException} se o tamaño do vector de datos é maior ca o espacio dispoñible
	 */
    public synchronized void add(short[] samples) {
        if (this.remaining() < samples.length) {
            throw new BufferOverflowException();
        }
        for (int i = 0; i < samples.length; i++) {
            this.data[this.in] = samples[i];
            this.incrementIn();
        }
    }

    /**
	 * Engade unha mostra ao canal
	 * 
	 * @param sample
	 *            A mostra a engadir
	 * @throws #{@link BufferOverflowException} se o buffer está cheo
	 */
    public synchronized void add(short sample) {
        if (this.isFull()) {
            throw new BufferOverflowException();
        }
        this.data[in] = sample;
        this.incrementIn();
    }

    /**
	 * 
	 * 
	 * @param lengthInSeconds
	 * @return
	 */
    public synchronized short[] read(int length) {
        if (this.getStored() < length) {
            throw new BufferUnderflowException();
        }
        short[] tmp = new short[length];
        for (int i = 0; i < length; i++) {
            tmp[i] = data[out];
            this.incrementOut();
        }
        return tmp;
    }

    /**
	 * Lee do canal as mostras correspondentes a o número de segundos especificado
	 * 
	 * @param lengthInSeconds
	 *            Segundos a ler
	 */
    public synchronized short[] readSeconds(int lengthInSeconds) {
        return read(this.frequency * lengthInSeconds);
    }

    /**
	 * Devuelve las muestras almacenadas en el canal
	 * 
	 * @return
	 */
    public synchronized short[] read() {
        return read(this.getStored());
    }

    /**
	 * Devuelve una muestra del canal
	 * 
	 * @return Una muestra del canal
	 */
    public synchronized short readSingle() {
        return read(1)[0];
    }

    /**
	 * Actualiza o punteiro de lectura e decrementa o contador de mostras despois de ler unha mostra
	 */
    private synchronized void incrementOut() {
        this.out = (this.out + 1) % this.capacity;
        this.fillCount--;
    }

    /**
	 * Actualiza o punteiro de escritura e incrementa o contador de mostras despois de ler unha mostra
	 */
    private synchronized void incrementIn() {
        this.in = (this.in + 1) % this.capacity;
        this.fillCount++;
    }

    /**
	 * Obtén o espacio dispoñible no canal de datos
	 * 
	 * @return O espacio dispoñible no canal en número de mostras
	 */
    public synchronized int remaining() {
        return this.capacity - fillCount;
    }

    /**
	 * Devolve o numero de mostras almacenadas
	 * 
	 * @return
	 */
    public synchronized int getStored() {
        return fillCount;
    }

    /**
	 * Comproba si o canal está valeiro
	 * 
	 * @return
	 */
    public synchronized boolean isEmpty() {
        return fillCount == 0;
    }

    /**
	 * 
	 * @return
	 */
    public synchronized boolean isFull() {
        return fillCount == capacity;
    }

    /**
	 * 
	 */
    public synchronized void clear() {
        in = 0;
        out = 0;
        fillCount = 0;
    }

    /**
	 * 
	 * @return
	 */
    public short getResolution() {
        return resolution;
    }

    /**
	 * 
	 * @param value
	 */
    public void setResolution(short value) {
        resolution = value;
    }

    /**
	 * 
	 * @return
	 */
    public short getFrequency() {
        return frequency;
    }

    /**
	 * 
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * 
	 * @param value
	 */
    public void setName(String value) {
        name = value;
    }

    /**
	 * 
	 * @return
	 */
    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Channel [");
        if (log != null) {
            builder.append("log=");
            builder.append(log);
            builder.append(", ");
        }
        if (name != null) {
            builder.append("name=");
            builder.append(name);
            builder.append(", ");
        }
        if (data != null) {
            builder.append("dataLength=");
            builder.append(data.length);
            builder.append(", ");
        }
        builder.append("resolution=");
        builder.append(resolution);
        builder.append(", frequency=");
        builder.append(frequency);
        builder.append(", in=");
        builder.append(in);
        builder.append(", out=");
        builder.append(out);
        builder.append(", capacity=");
        builder.append(capacity);
        builder.append(", fillCount=");
        builder.append(fillCount);
        builder.append("]");
        return builder.toString();
    }

    public static class Builder {

        private String name;

        private short resolution;

        private short frequency;

        private int capacityInSeconds;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setResolution(short resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder setFrequency(short frequency) {
            this.frequency = frequency;
            return this;
        }

        public Builder setCapacityInSeconds(int capacityInSeconds) {
            this.capacityInSeconds = capacityInSeconds;
            return this;
        }

        public Channel build() {
            return new Channel(resolution, frequency, name, capacityInSeconds);
        }
    }

    public static class ArrayBuilder {

        private List<MITSignalSpecification> signals;

        private int capacityInSeconds;

        public ArrayBuilder(int capacityInSeconds) {
            signals = new ArrayList<MITSignalSpecification>();
            this.capacityInSeconds = capacityInSeconds;
        }

        public ArrayBuilder() {
            signals = new ArrayList<MITSignalSpecification>();
        }

        public ArrayBuilder setCapacityInSeconds(int capacityInSeconds) {
            this.capacityInSeconds = capacityInSeconds;
            return this;
        }

        public ArrayBuilder setSignals(List<MITSignalSpecification> sigs) {
            this.signals = sigs;
            return this;
        }

        public ArrayBuilder addSignal(MITSignalSpecification sig) {
            this.signals.add(sig);
            return this;
        }

        public Channel[] build() {
            Channel[] channels = new Channel[signals.size()];
            for (int i = 0; i < channels.length; i++) {
                channels[i] = new Channel.Builder().setName(signals.get(i).getDescription()).setFrequency((short) signals.get(i).getSampleFrequency()).setResolution(signals.get(i).getADCResolution().shortValue()).setCapacityInSeconds(capacityInSeconds).build();
            }
            return channels;
        }
    }
}
