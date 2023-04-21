package com.android.apicheck;

import java.util.*;

public class ApiInfo {

    private HashMap<String, PackageInfo> mPackages;

    private HashMap<String, ClassInfo> mAllClasses;

    public ApiInfo() {
        mPackages = new HashMap<String, PackageInfo>();
        mAllClasses = new HashMap<String, ClassInfo>();
    }

    public boolean isConsistent(ApiInfo otherApi) {
        boolean consistent = true;
        for (PackageInfo pInfo : mPackages.values()) {
            if (otherApi.getPackages().containsKey(pInfo.name())) {
                if (!pInfo.isConsistent(otherApi.getPackages().get(pInfo.name()))) {
                    consistent = false;
                }
            } else {
                Errors.error(Errors.REMOVED_PACKAGE, pInfo.position(), "Removed package " + pInfo.name());
                consistent = false;
            }
        }
        for (PackageInfo pInfo : otherApi.mPackages.values()) {
            if (!pInfo.isInBoth()) {
                Errors.error(Errors.ADDED_PACKAGE, pInfo.position(), "Added package " + pInfo.name());
                consistent = false;
            }
        }
        return consistent;
    }

    public HashMap<String, PackageInfo> getPackages() {
        return mPackages;
    }

    public void addPackage(PackageInfo pInfo) {
        mPackages.put(pInfo.name(), pInfo);
        for (ClassInfo cl : pInfo.allClasses().values()) {
            mAllClasses.put(cl.qualifiedName(), cl);
        }
    }

    public void resolveSuperclasses() {
        for (ClassInfo cl : mAllClasses.values()) {
            if (!cl.qualifiedName().equals("java.lang.Object")) {
                String scName = cl.superclassName();
                if (scName == null) {
                    scName = "java.lang.Object";
                }
                ClassInfo superclass = mAllClasses.get(scName);
                cl.setSuperClass(superclass);
            }
        }
    }
}
