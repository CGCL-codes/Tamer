package net.algid.purchase.valueObject;

/**
 * ValueObject ��� ������ �� ������� "������"
 * @author Satiricon
 */
public class Good {

    public int code = Integer.MIN_VALUE;

    public int supplierRFCode = Integer.MIN_VALUE;

    public String nomarticle = null;

    public String name = null;

    public static final String tableName = "GOOD";

    public static final String codeField = "CODE";

    public static final String supplierRFCodeField = "SUPPLIERRFCODE";

    public static final String nomarticleField = "NOMARTICLE";

    public static final String nameField = "NAME";
}
