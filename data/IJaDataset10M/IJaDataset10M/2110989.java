package what.api;

import java.util.ArrayList;
import java.util.List;

public class ApiLogDiff {

    public ApiLog log1, log2, newLog, removedLog;

    public ApiLogDiff(ApiLog log1, ApiLog log2) {
        this.log1 = log1;
        this.log2 = log2;
    }

    public void resolve() {
        newLog = compare(log1, log2);
        removedLog = compare(log2, log1);
    }

    /**
	 * Returns all apis in log1 not in log2.
	 */
    static ApiLog compare(ApiLog log1, ApiLog log2) {
        ApiLog result = new ApiLog(String.format("Api's in %s not in %s", log1.description, log2.description));
        List<ApiPackage> apiPackagePath1 = new ArrayList<ApiPackage>();
        List<ApiPackage> apiPackagePath2 = new ArrayList<ApiPackage>();
        for (ApiPackage apiPackage : log1.log.values()) {
            apiPackagePath1.add(apiPackage);
            ApiPackage temp = log2.log.get(apiPackage.packageName);
            if (temp != null) apiPackagePath2.add(temp);
            compare(apiPackagePath1, log1, apiPackagePath2, log2, result);
            apiPackagePath1.clear();
            apiPackagePath2.clear();
        }
        result.updateCount(result.log.values());
        return result;
    }

    /**
	 * recursively compare given package and its subfolders with matching in
	 * other log
	 */
    static void compare(List<ApiPackage> apiPackagePath1, ApiLog log1, List<ApiPackage> apiPackagePath2, ApiLog log2, ApiLog result) {
        ApiPackage apiPackage1 = apiPackagePath1.get(apiPackagePath1.size() - 1);
        ApiPackage apiPackage2 = apiPackagePath2.size() == apiPackagePath1.size() ? apiPackagePath2.get(apiPackagePath2.size() - 1) : null;
        for (ApiClass apiClass : apiPackage1.classes.values()) {
            ApiClass apiClass2 = apiPackage2 != null ? apiPackage2.classes.get(apiClass.className) : null;
            for (ApiMethod apiMethod : apiClass.methods.values()) {
                ApiMethod apiMethod2 = apiClass2 != null ? apiClass2.methods.get(apiMethod.name) : null;
                for (ApiSignature apiSignature : apiMethod.signatures.values()) {
                    ApiSignature apiSignature2 = apiMethod2 != null ? apiMethod2.signatures.get(apiSignature.signature) : null;
                    if (apiSignature2 == null) {
                        ApiPackage apiPackageNew = result.log.get(apiPackage1.packageName);
                        if (apiPackageNew == null) {
                            apiPackageNew = result.createApiPackage(null, getFullPackageName(apiPackagePath1));
                        }
                        ApiClass apiClassNew = apiPackageNew.classes.get(apiClass.className);
                        if (apiClassNew == null) {
                            apiClassNew = new ApiClass(apiPackageNew, apiClass.className);
                            apiPackageNew.classes.put(apiClassNew.className, apiClassNew);
                        }
                        ApiMethod apiMethodNew = apiClassNew.methods.get(apiMethod.name);
                        if (apiMethodNew == null) {
                            apiMethodNew = new ApiMethod(apiClass, apiMethod.name);
                            apiClassNew.methods.put(apiMethodNew.name, apiMethodNew);
                        }
                        ApiSignature apiSignatureNew = apiMethodNew.signatures.get(apiSignature.signature);
                        if (apiSignatureNew == null) {
                            apiSignatureNew = new ApiSignature(apiMethodNew, apiSignature.signature);
                            apiMethodNew.signatures.put(apiSignatureNew.signature, apiSignature);
                        }
                    }
                }
            }
        }
        for (ApiPackage subPackage1 : apiPackage1.packages.values()) {
            apiPackagePath1.add(subPackage1);
            ApiPackage subPackage2 = apiPackage2 != null ? apiPackage2.packages.get(subPackage1.packageName) : null;
            if (subPackage2 != null) apiPackagePath2.add(subPackage2);
            compare(apiPackagePath1, log1, apiPackagePath2, log2, result);
            apiPackagePath1.remove(apiPackagePath1.size() - 1);
            if (subPackage2 != null) apiPackagePath2.remove(apiPackagePath2.size() - 1);
        }
    }

    static String getFullPackageName(List<ApiPackage> apiPackagePath) {
        String result = "";
        for (ApiPackage apiPackage : apiPackagePath) result += apiPackage.packageName + ".";
        return result.substring(0, result.length() - 1);
    }
}
