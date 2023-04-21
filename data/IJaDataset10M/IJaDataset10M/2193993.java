package org.neblipedia.empaquetar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Desempaquetador {

    private final int BUFFER = 4096;

    protected File destino;

    protected String nombre;

    protected File origen;

    protected long zip_actual = 0;

    private ZipFile zis;

    public Desempaquetador(File nbz) {
        this.origen = nbz.getParentFile();
        nombre = nbz.getName();
        nombre = nombre.substring(0, nombre.lastIndexOf('-'));
    }

    public void close() throws IOException {
        zis.close();
    }

    private void crearZif() throws EmpaquetadorException {
        try {
            File file = new File(origen, nombre + "-" + zip_actual + ".nbz");
            zip_actual++;
            zis = new ZipFile(file);
            System.out.println("extrayendo " + file.getName());
        } catch (IOException e) {
            throw new EmpaquetadorException("nbz no disponible");
        }
    }

    public void desempaquetar() {
        try {
            while (true) {
                crearZif();
                Enumeration<? extends ZipEntry> entries = zis.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry ze = entries.nextElement();
                    String nombre = ze.getName();
                    System.out.println("extrae: " + nombre);
                    File f = new File(destino, nombre);
                    f.getParentFile().mkdirs();
                    try {
                        FileOutputStream dest = new FileOutputStream(f, true);
                        byte data[] = new byte[BUFFER];
                        InputStream is = zis.getInputStream(ze);
                        int count;
                        while ((count = is.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, count);
                        }
                        is.close();
                        dest.flush();
                        dest.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (EmpaquetadorException e) {
            System.out.println("fin");
        }
    }

    public File getDestino() {
        return destino;
    }

    public void setDestino(File destino) {
        this.destino = destino;
        this.destino.mkdirs();
    }
}
