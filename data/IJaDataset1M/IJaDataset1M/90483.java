package l1j.server;

import java.io.IOException;
import java.util.Map;
import l1j.server.server.model.map.L1Map;

/**
 * ���� �о���̱� ������(����)�� �߻� Ŭ����.
 */
public abstract class MapReader {

    /**
	 * ��� �ؽ�Ʈ ���� �о���δ�(�߻� Ŭ����).
	 * 
	 * @return Map
	 * @throws IOException
	 */
    public abstract Map<Integer, L1Map> read() throws IOException;

    /**
	 * ������ �� ��ȣ�� �ؽ�Ʈ ���� �о���δ�.
	 * 
	 * @param id
	 *            �� ID
	 * @return L1Map
	 * @throws IOException
	 */
    public abstract L1Map read(int id) throws IOException;

    /**
	 * �о���̴� �� ������ �Ǵ��Ѵ�(�ؽ�Ʈ �� or ĳ�� �� or V2�ؽ�Ʈ ��).
	 * 
	 * @return MapReader
	 */
    public static MapReader getDefaultReader() {
        if (Config.LOAD_V2_MAP_FILES) {
            return new V2MapReader();
        }
        if (Config.CACHE_MAP_FILES) {
            return new CachedMapReader();
        }
        return new TextMapReader();
    }
}
