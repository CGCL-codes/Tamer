package org.openthinclient.pkgmgr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openthinclient.pkgmgr.connect.ConnectToServer;
import org.openthinclient.pkgmgr.connect.SearchForServerFile;
import org.openthinclient.util.dpkg.Package;
import org.openthinclient.util.dpkg.PackageDatabase;
import org.openthinclient.util.dpkg.UrlAndFile;
import com.levigo.util.preferences.PreferenceStoreHolder;

/**
 * removes the actually cache database, connect to the internet load the newest
 * Packages.gz files (which are given in the sources.list file) and make a new
 * database out of the Packages which are in these files, and not already
 * installed.
 * 
 * @author tauschfn
 * 
 */
public class UpdateDatabase {

    private static String cacheDatabase;

    private static String changelogDir;

    private static final Logger logger = Logger.getLogger(UpdateDatabase.class);

    public UpdateDatabase(String cacheDatabase, String chlogDir) {
        UpdateDatabase.cacheDatabase = cacheDatabase;
        changelogDir = chlogDir;
    }

    public UpdateDatabase() {
    }

    public PackageDatabase doUpdate(boolean isStart) throws PackageManagerException {
        if (!isStart) try {
            final PackageDatabase packDB = PackageDatabase.open(new File(cacheDatabase));
            packDB.save();
            return packDB;
        } catch (final IOException e) {
            logger.error(e);
            throw new PackageManagerException(e);
        } else {
            List<Package> packages;
            PackageDatabase packDB;
            List<UrlAndFile> updatedFiles = null;
            final SearchForServerFile seFoSeFi = new SearchForServerFile();
            updatedFiles = seFoSeFi.checkForNewUpdatedFiles(null);
            if (null == updatedFiles) throw new PackageManagerException(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("interface.noFilesAvailable", "No entry found for interface.noFilesAvailable"));
            packages = new ArrayList<Package>();
            for (int i = 0; i < updatedFiles.size(); i++) try {
                final List<Package> packageList = new ArrayList<Package>();
                packageList.addAll(new DPKGPackageFactory(null).getPackage(updatedFiles.get(i).getFile()));
                for (final Package pkg : packageList) {
                    packages.add(pkg);
                    final Package p = packages.get(packages.size() - 1);
                    p.setServerPath(updatedFiles.get(i).getUrl());
                    p.setChangelogDir(updatedFiles.get(i).getChangelogDir());
                    downloadChangelogFile(null, pkg, changelogDir, updatedFiles.get(i).getChangelogDir());
                }
            } catch (final IOException e) {
                e.printStackTrace();
                throw new PackageManagerException(e);
            }
            packDB = null;
            if ((new File(cacheDatabase)).isFile()) (new File(cacheDatabase)).delete();
            try {
                packDB = PackageDatabase.open(new File(cacheDatabase));
            } catch (IOException e1) {
                e1.printStackTrace();
                logger.error(e1);
                throw new PackageManagerException(e1);
            }
            for (int i = 0; i < packages.size(); i++) {
                if (!packDB.isPackageInstalled(packages.get(i).getName())) {
                    packDB.addPackage(packages.get(i));
                    continue;
                }
                final int n = packDB.getPackage(packages.get(i).getName()).getVersion().compareTo(packages.get(i).getVersion());
                if (n == -1) packDB.addPackage(packages.get(i));
            }
            try {
                packDB.save();
            } catch (IOException e) {
                logger.error(e);
                e.printStackTrace();
                throw new PackageManagerException(e);
            }
            return packDB;
        }
    }

    public PackageDatabase doUpdate(PackageManager pm) throws PackageManagerException {
        List<Package> packages;
        PackageDatabase packDB;
        List<UrlAndFile> updatedFiles = null;
        final SearchForServerFile seFoSeFi = new SearchForServerFile();
        updatedFiles = seFoSeFi.checkForNewUpdatedFiles(pm);
        if (null == updatedFiles) throw new PackageManagerException(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("interface.noFilesAvailable", "No entry found for interface.noFilesAvailable"));
        packages = new ArrayList<Package>();
        for (int i = 0; i < updatedFiles.size(); i++) try {
            final List<Package> packageList = new ArrayList<Package>();
            packageList.addAll(new DPKGPackageFactory(pm).getPackage(updatedFiles.get(i).getFile()));
            for (final Package pkg : packageList) {
                packages.add(pkg);
                final Package p = packages.get(packages.size() - 1);
                p.setServerPath(updatedFiles.get(i).getUrl());
                p.setChangelogDir(updatedFiles.get(i).getChangelogDir());
                downloadChangelogFile(pm, pkg, changelogDir, updatedFiles.get(i).getChangelogDir());
            }
        } catch (final IOException e) {
            String errormessage = PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("UpdateDatabase.doUpdate.GeneratePackages.IOException", "No entry found for UpdateDatabase.doUpdate.GeneratePackages.IOException");
            if (null != pm) pm.addWarning(errormessage + e);
            logger.error(errormessage, e);
            e.printStackTrace();
            throw new PackageManagerException(e);
        }
        packDB = null;
        if ((new File(cacheDatabase)).isFile()) (new File(cacheDatabase)).delete();
        try {
            packDB = PackageDatabase.open(new File(cacheDatabase));
        } catch (IOException e1) {
            e1.printStackTrace();
            logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("UpdateDatabase.doUpdate.OpenDatabase.IOException", "No entry found for UpdateDatabase.doUpdate.OpenDatabase.IOException"), e1);
        }
        for (int i = 0; i < packages.size(); i++) {
            if (!packDB.isPackageInstalled(packages.get(i).getName())) {
                packDB.addPackage(packages.get(i));
                continue;
            }
            final int n = packDB.getPackage(packages.get(i).getName()).getVersion().compareTo(packages.get(i).getVersion());
            if (n == -1) packDB.addPackage(packages.get(i));
        }
        try {
            packDB.save();
        } catch (IOException e) {
            logger.error(PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("UpdateDatabase.doUpdate.SaveDatabase.IOException", "No entry found for UpdateDatabase.doUpdate.SaveDatabase.IOException"), e);
            e.printStackTrace();
        }
        return packDB;
    }

    private static boolean downloadChangelogFile(PackageManager pm, Package pkg, String changelogDirectory, String changeDir) throws PackageManagerException {
        boolean ret = false;
        try {
            final File changelogDir = new File((new StringBuilder()).append(changelogDirectory).append(changeDir).toString());
            String serverPath = pkg.getServerPath();
            serverPath = serverPath.substring(0, serverPath.lastIndexOf("/") + 1);
            final BufferedInputStream in = new BufferedInputStream(new ConnectToServer(pm).getInputStream((new StringBuilder()).append(serverPath).append(pkg.getName()).append(".changelog").toString()));
            if (!changelogDir.isDirectory()) changelogDir.mkdirs();
            final File rename = new File(changelogDir.getCanonicalPath(), (new StringBuilder()).append(pkg.getName()).append(".changelog").toString());
            final FileOutputStream out = new FileOutputStream(rename);
            final byte buf[] = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            in.close();
            ret = true;
        } catch (final Exception e) {
            if (null != pm) {
                logger.warn(e);
                pm.addWarning(e.toString());
            } else logger.warn(e);
        }
        return ret;
    }
}
