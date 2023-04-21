package Test.LoaiNguoiDungDao;

import static org.junit.Assert.assertTrue;
import hcmus.am.client.entity.ChiTietNhomNguoiDungEntity;
import hcmus.am.client.entity.LoaiNguoiDungEntity;
import hcmus.am.dao.ChiTietNhomNguoiDungDao;
import hcmus.am.dao.LoaiNguoiDungDao;
import org.junit.Test;

public class insert {

    @Test
    public void testSelectNguoiDungCuaNhom() {
        LoaiNguoiDungEntity ent = LoaiNguoiDungDao.selectById(1);
        ent.Ten = "loai thu 3";
        int rs = LoaiNguoiDungDao.insert(ent);
        assertTrue(rs == 1);
    }
}
