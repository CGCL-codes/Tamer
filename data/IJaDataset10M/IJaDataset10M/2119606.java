package ar.com.datos.dataaccess.trie;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ar.com.datos.dataaccess.api.ArchivoBloque;
import ar.com.datos.dataaccess.dto.BloqueRegistro;
import ar.com.datos.dataaccess.exceptions.NoSePuedeGrabarRegistroAdminException;
import ar.com.datos.dataaccess.exceptions.NoSePuedeGrabarRegistroException;
import ar.com.datos.dataaccess.trie.dto.RegistroTrie;

public class ArchivoTrie extends ArchivoBloque {

    public static final String RUTA_ARCH = ArchivoBloque.getParameter("ArchivoTrie.RutaArchivo", "./files/ArchivoTrie.trie");

    public static final int PROFUNDIDAD = (int) ArchivoBloque.getNumParameter("ArchivoTrie.Profundidad", 4);

    public static final int TAMANIO_PARTICION = (int) ArchivoBloque.getNumParameter("ArchivoTrie.TamanioParticion", 25);

    public static final int BYTES_NODO_TRIE = (int) ArchivoBloque.getNumParameter("ArchivoTrie.BytesNodoTrie", 19);

    public static final int TAMANIO_BLOQUE = (int) ArchivoBloque.getNumParameter("ArchivoTrie.Tama�oBloque", 32768);

    private int profundidad;

    private int tamanioParticion;

    private int bytesNodoTrie;

    BloqueRegistro bloqueActual;

    int idBloqueActual;

    public ArchivoTrie() throws FileNotFoundException, NoSePuedeGrabarRegistroAdminException {
        super(ArchivoTrie.RUTA_ARCH, "rw");
    }

    @Override
    protected boolean initFirstBlock() {
        return true;
    }

    @Override
    protected List<Object> getAdminParameters() {
        List<Object> params = new ArrayList<Object>();
        params.add(new Integer(ArchivoTrie.PROFUNDIDAD));
        params.add(new Integer(ArchivoTrie.TAMANIO_PARTICION));
        params.add(new Integer(ArchivoTrie.BYTES_NODO_TRIE));
        this.profundidad = ArchivoTrie.PROFUNDIDAD;
        this.tamanioParticion = ArchivoTrie.TAMANIO_PARTICION;
        this.bytesNodoTrie = ArchivoTrie.BYTES_NODO_TRIE;
        this.bloqueActual = new BloqueRegistro(0, getTamanioBloqueArchivo());
        this.idBloqueActual = -1;
        return params;
    }

    @Override
    protected int getTamanioBloqueArchivo() {
        return ArchivoTrie.TAMANIO_BLOQUE;
    }

    @Override
    protected void cargaInfoAdminPartic(DataInputStream dis) throws IOException {
        this.profundidad = dis.readInt();
        this.tamanioParticion = dis.readInt();
        this.bytesNodoTrie = dis.readInt();
        this.bloqueActual = new BloqueRegistro(0, getBlockSize());
        this.idBloqueActual = -1;
    }

    @Override
    protected void cargarInfoInicialBloque(BloqueRegistro block0) throws NoSePuedeGrabarRegistroException {
        RegistroTrie reg0 = new RegistroTrie(block0.getIdBloque(), 0);
        RegistroTrie reg1 = new RegistroTrie(block0.getIdBloque(), 1);
        reg0.setBloqueHno(reg1.getNroBloque());
        reg0.setRegHno(reg1.getNroRegistro());
        block0.agregarRegistro(reg0.serializar());
        block0.agregarRegistro(reg1.serializar());
    }

    @Override
    public String toString() {
        return "[ArchivoTrie]:[profundidad=" + profundidad + "; tama�oParticion=" + tamanioParticion + "; bytesNodoTrie=" + bytesNodoTrie + "]";
    }

    @Override
    protected void finalize() throws Throwable {
        cerrar();
        super.finalize();
    }

    public int getProfundidad() {
        return profundidad;
    }
}
