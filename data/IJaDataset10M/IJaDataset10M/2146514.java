package com.pos.spatobiz.app.controller.karyawan;

import com.pos.spatobiz.app.util.SpringUtilities;
import com.pos.spatobiz.app.view.karyawan.UbahKaryawan;
import com.pos.spatobiz.common.dao.KaryawanDao;
import com.pos.spatobiz.common.entity.Karyawan;
import echo.gokil.desktop.util.DesktopManager;
import echo.gokil.desktop.util.DesktopUtilities;
import echo.gokil.desktop.worker.DesktopWorker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author echo
 */
public class UbahKaryawanController implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        new UbahWorker(true).execute();
    }

    public class UbahWorker extends DesktopWorker<Karyawan, Void> {

        public UbahWorker(boolean blockInput) {
            super(blockInput);
        }

        @Override
        public boolean beforeDone() {
            try {
                Karyawan karyawan = get();
                KaryawanDao karyawanDao = (KaryawanDao) SpringUtilities.getApplicationContext().getBean("karyawanDao");
                karyawanDao.updateKaryawan(karyawan);
                return true;
            } catch (Exception e) {
                DesktopUtilities.showErrorMessage(DesktopManager.getApplication(), "SpatoBiz 1.0", e.getMessage());
                return false;
            }
        }

        @Override
        public void afterDone() {
            DesktopUtilities.showInfoMessage(DesktopManager.getApplication(), "SpatoBiz 1.0", "Pengubahan Karyawan Sukses");
            DesktopManager.getApplication().showChildPane("menuKaryawan");
        }

        @Override
        protected Karyawan doInBackground() throws Exception {
            UbahKaryawan ubahKaryawan = (UbahKaryawan) DesktopManager.getPanel("ubahKaryawan");
            Karyawan karyawan = ubahKaryawan.getKaryawan();
            return karyawan;
        }
    }
}
