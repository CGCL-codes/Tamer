package serialization;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * 
 * ������� ������� ������ ������ ����������, ������� ������������ ��������� 
 * ����� ������������, ����� ��� ������� ��� �������������� ���� ������� � �����
 * ��������������.
 * NewMarshaler2Factory ������� ������� ���� Marshaler, ������� ����� ����� ������ � ����������
 * (�������� �� ��������� ������� ������������)
 */
public class NewMarshaler2Factory implements MarshalerFactory {

    /** ������� ������������ ������� ��������� ������ � ������������ ���� ������� � ����. */
    private HashMap<Class<?>, String> CANONICAL_NAMES_CLASSES = new HashMap<Class<?>, String>();

    /** ������� ������������ ������������ ���� ������� �� ��������� ������*/
    private HashMap<String, Class<?>> LOCAL_NAMES_CLASSES = new HashMap<String, Class<?>>();

    private HashMap<Class<?>, ClassSerializator> hs_ClassSerializators = new HashMap<Class<?>, ClassSerializator>();

    /** ��� �������, �������� �� ������ ������� ���������� �������� ������� ������.
	 *  ������� ������������: 
	 *  3 - ������� - � ������� ������-�������������, 
	 *  2 - ���������� - ���������� ������ � ������� ���������� ������� ���������
	 *  1 - ����������� - ����� ObjectOutputStream.writeObject(obj)
	 *  ������, � ��������������� ����� ����� �� ��������� ��� ������������ ������ �������?
	 */
    private HashMap<Class<?>, Integer> hs_cache = new HashMap<Class<?>, Integer>();

    /**
	 * ���� �� ������ ������� ��� ������� ��������� ��� �������...
	 * �������� ������� ���� �� ������� �� ��� HashMap<String, String>
	 * ����� ������� ��� ��������� ������ ����� ���� �� ��������� ��������
	 * ����� �� ����� ����������� �������������� ClassSerializator ��� ������� ��� ���� final...
	 * ���� final �� ��� �����.. �� ���� ������������ � ��������������� � ������ - ������� ������ ��� ������.
	 * ���� final ��� �� ����� �� ������, �.�. ����������� ������� ������������, � ������ �� ���������� �����������
	 * �� �� ����� ����� ����������� ���������� ������� �� ������ ������
	 * ������ �� ����� ������� ��� ��������� ������.
	 * 
	 * 
	 * @param CNC
	 * @param LNC
	 * @param hs_ClassSer
	 * 
	 */
    public NewMarshaler2Factory(HashMap<Class<?>, String> CNC, HashMap<Class<?>, ClassSerializator> hs_ClassSer) {
        CANONICAL_NAMES_CLASSES = CNC;
        for (Class<?> clazz : CANONICAL_NAMES_CLASSES.keySet()) {
            String CCN = CANONICAL_NAMES_CLASSES.get(clazz);
            LOCAL_NAMES_CLASSES.put(CCN, clazz);
        }
        hs_ClassSerializators = hs_ClassSer;
    }

    public NewMarshaler2Factory() {
    }

    public Marshaler createMarshaler() {
        return new NewMarshaler2(this);
    }

    /**
	 * ���������� ������������ ��� ������ � ������������ � �������� CANONICAL_NAMES_CLASSES.
	 * ���� � ������� ��� ������� ��� ������� ������, �� ���������� �������� ��� ������.
	 * @param cls - �����
	 * @return - ������������ ��� ������ � ����
	 */
    public String getCanonicalName(Class<?> cls) {
        String canonicalName = CANONICAL_NAMES_CLASSES.get(cls);
        if (canonicalName != null) {
            return canonicalName;
        } else {
            return cls.getName();
        }
    }

    /**
	 * ���������� ����� �� ��������� ������ ��������������� ������������� ����� ������ � ����
	 * @param canonicalName - ������������ ��� ������ � ����
	 * @return ����� ��������� ������, ��������������� ������� �����
	 * @throws ClassNotFoundException
	 */
    public Class<?> getLocalClass(String canonicalName) throws ClassNotFoundException {
        Class<?> cls = LOCAL_NAMES_CLASSES.get(canonicalName);
        if (cls != null) {
            return cls;
        } else {
            return Class.forName(canonicalName);
        }
    }

    /**
	 * 
	 * @param cls �����
	 * @return �����-������������, ���� �� ���� ��� null 
	 */
    public ClassSerializator getClassSerializator(Class<?> cls) {
        return hs_ClassSerializators.get(cls);
    }

    public int getModeSerialization(Class<?> cls) {
        Integer mode = hs_cache.get(cls);
        if (mode != null) {
            return mode;
        }
        ClassSerializator serializator = getClassSerializator(cls);
        if (serializator != null) {
            hs_cache.put(cls, new Integer(3));
            return 3;
        }
        try {
            Constructor constr = cls.getDeclaredConstructor(new Class[0]);
            constr.setAccessible(true);
            hs_cache.put(cls, new Integer(2));
            return 2;
        } catch (Exception ex) {
            hs_cache.put(cls, new Integer(1));
            return 1;
        }
    }
}
