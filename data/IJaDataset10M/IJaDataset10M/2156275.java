package Steganography.BlakleyScheme;

import Steganography.BlakleyScheme.ImageTools.LCGenerator;
import Abstract.EmbeddingProperties;
import Exception.StegoException;
import Steganography.BlakleyScheme.ImageTools.IBB;
import com.sun.org.apache.xalan.internal.xsltc.dom.BitArray;
import image.png.PngTextMetadataUnit;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Key-mask
 * @author Олеся
 */
public class BlakleySchemeEmbeddingPropeties extends EmbeddingProperties {

    private byte m_data[] = null;

    private String MAIN_DATA = "date";

    private Integer m_starts[] = null;

    private String START_MEANS = "starts";

    public static final int m_size = 22;

    @Override
    public boolean correct() {
        return ((m_data != null) && (m_starts != null));
    }

    @Override
    public LinkedList<PngTextMetadataUnit> getPropsList() {
        LinkedList<PngTextMetadataUnit> list = new LinkedList<PngTextMetadataUnit>();
        PngTextMetadataUnit DataUnit = new PngTextMetadataUnit(MAIN_DATA, new String(m_data));
        list.add(DataUnit);
        for (int i = 0; i < m_starts.length; i++) {
            PngTextMetadataUnit StartsUnit = new PngTextMetadataUnit(START_MEANS + i, String.valueOf(m_starts[i]));
            list.add(StartsUnit);
        }
        return list;
    }

    @Override
    public void addProps(PngTextMetadataUnit unit) {
        if (unit.keyword.equals(MAIN_DATA)) {
            m_data = unit.value.getBytes();
        } else if (unit.keyword.contains(START_MEANS)) {
            LinkedList<Integer> list = new LinkedList<Integer>();
            if (m_starts != null) list.addAll(Arrays.asList(m_starts));
            list.add(Integer.valueOf(unit.value));
            m_starts = list.toArray(new Integer[0]);
        }
    }

    public BlakleySchemeEmbeddingPropeties() {
        m_data = new byte[m_size];
    }

    /**
     * Constructor to create key-mask requared length and keeped some parameters
     * @param D size of square
     * @param K first parameter
     * @param M second parameter
     * @param SizeCross count of pixel on cross's side
     * @param CountRepeat count of repeat every bit
     * @param gen parameters of LinearCongruentGenerator
     */
    public BlakleySchemeEmbeddingPropeties(int K, int M, int D, LCGenerator gen, byte SizeCross, byte NumberRepeat) {
        m_data = new byte[m_size];
        for (int i = 0; i < 4; i++) {
            m_data[i] = (IBB.intToByteArray(K))[i];
            m_data[i + 4] = (IBB.intToByteArray(M))[i];
            m_data[i + 8] = (IBB.intToByteArray(D))[i];
            m_data[i + 12] = (IBB.intToByteArray(gen.get_a()))[i];
            m_data[i + 16] = (IBB.intToByteArray(gen.get_c()))[i];
        }
        m_data[20] = SizeCross;
        m_data[21] = NumberRepeat;
        m_starts = new Integer[M];
    }

    /**
     * Constructor to create the copy
     * @param key copied key-mask
     */
    public BlakleySchemeEmbeddingPropeties(BlakleySchemeEmbeddingPropeties key) {
        this.m_data = key.m_data;
        this.m_starts = key.m_starts;
    }

    /**
     * Set one of the start means
     * @param i number of start mean
     * @param N0 value of start mean
     * @throws ExeptionParameters
     */
    public void set_Start(int i, int N0) throws StegoException {
        if (i < 0 || i >= get_M()) throw new StegoException("The input number of start mean is not correct");
        m_starts[i] = N0;
    }

    public void set_Starts(LinkedList<String> list) throws StegoException {
        if (list.size() != get_M()) throw new StegoException();
        m_starts = new Integer[list.size()];
        for (int i = 0; i < m_starts.length; i++) m_starts[i] = Integer.valueOf(list.pollFirst());
    }

    public void set_Data(String data) throws StegoException {
        char help[] = data.toCharArray();
        if (help.length != m_size) throw new StegoException();
        for (int i = 0; i < m_size; i++) m_data[i] = (byte) help[i];
    }

    /**
     * Set one of the start means
     * @param i number of start mean
     * @return  one of the start means
     * @throws ExeptionParameters
     */
    public int get_Start(int i) throws StegoException {
        if (i < 0 || i >= get_M()) throw new StegoException("The input number of start mean is not correct");
        return m_starts[i];
    }

    public String get_StartString(int i) throws StegoException {
        if (i < 0 || i >= get_M()) throw new StegoException("The input number of start mean is not correct");
        return String.valueOf(m_starts[i]);
    }

    /**
     * Get foundation of key-mask as array of bits
     * @return key-mask as array of bits
     */
    public BitArray get_data() {
        return IBB.ByteArrayToBitArray(m_data, m_size, m_size * 8);
    }

    /**
     * Get foundation of key-mask as array of byte
     * @return key-mask as array of byte
     */
    public String get_DataString() {
        char help[] = new char[m_size];
        for (int i = 0; i < m_size; i++) help[i] = (char) m_data[i];
        return String.valueOf(help);
    }

    /**
     * Get the first parameter of scheme
     * @return the first parameter of scheme
     */
    public int get_K() {
        return (get_i_Int(0));
    }

    /**
     * Get the second parameter of scheme
     * @return the second parameter of scheme
     */
    public int get_M() {
        return (get_i_Int(4));
    }

    /**
     * Get the size of square
     * @return the size of square
     */
    public int get_D() {
        return (get_i_Int(8));
    }

    /**
     * Get the parameter A of the linear generator
     * @return the parameter A of the linear generator
     */
    public int get_Gen_a() {
        return (get_i_Int(12));
    }

    /**
     * Get the parameter C of the linear generator
     * @return the parameter C of the linear generator
     */
    public int get_Gen_c() {
        return (get_i_Int(16));
    }

    /**
     * Get the size of the cross'side
     * @return the size of the cross'side
     */
    public byte get_SizeCross() {
        return m_data[20];
    }

    /**
     * Get the number of pixel' repeat
     * @return the number of pixel' repeat
     */
    public byte get_NumberRepeat() {
        return m_data[21];
    }

    private int get_i_Int(int i) {
        byte help[] = { m_data[i], m_data[i + 1], m_data[i + 2], m_data[i + 3] };
        return (new BigInteger(help)).intValue();
    }
}
