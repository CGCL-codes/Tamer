package atp.reporter.items.allocators.cits;

import atp.reporter.db.*;
import atp.reporter.items.allocators.RAllocatorObjects;

public class RAllocatorObjectsCITS extends RAllocatorObjects {

    /**
	 * ��������������� ������ ������� ��������
	 * @param cdng ���
	 * @return SQL ������
	 */
    protected String getSQL(String cdng) {
        return "select parent, id, fid_type_obj, name from " + (cdng == null ? "" : "vIdentify v, ") + "object o_0 where fid_type_obj=?" + (cdng == null ? "" : "and o_0.Id between V.Id_Rep*10000 and (V.Id_Rep+1)*10000-1 and V.Name_Dept like '����-'||?");
    }

    public RDBType getDBType() {
        return RDBType.CITS;
    }
}
