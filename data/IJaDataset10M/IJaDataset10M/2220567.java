package ces.platform.bbs.option;

public interface Upgrader {

    String[] VERSIONS = { "1.0", "1.1", "1.2", "2.0" };

    String[][] UPGRADE_TYPE = { { "1.0", "2.0" }, { "1.1", "2.0" }, { "1.2", "2.0" } };

    int UPGRADE_FROM_10_TO_20 = 0;

    int UPGRADE_FROM_11_TO_20 = 1;

    int UPGRADE_FROM_12_TO_20 = 2;

    /**
     * ����һ����
     * @return �ɹ���ļ�¼��
     * @throws Exception
     */
    int upgradeNextTable() throws Exception;

    /**
     *
     * @return
     */
    boolean hasTableToUpgrade();

    /**
     * �ؽ���һ���������
     * @return �ؽ����������
     * @throws Exception
     */
    int indexNextTable() throws Exception;

    /**
     *
     * @return ��Ҫ�ؽ�����
     */
    boolean needRebuildIndex();

    /**
     *
     * @return ���ڴ���ı���
     */
    String getCurrentTable();

    /**
     *
     * @return ��һ������ı���
     */
    String getNextTable();

    /**
     *
     *
     */
    void resetIndex();
}
