package se.vgregion.webbisar.helpers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.vgregion.webbisar.beans.MainWebbisBean;
import se.vgregion.webbisar.svc.WebbisImageService;
import se.vgregion.webbisar.svc.WebbisService;
import se.vgregion.webbisar.types.Webbis;

public class WebbisServiceProxy {

    private WebbisService webbisService;

    private WebbisImageService webbisImageService;

    public WebbisServiceProxy() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "services-config.xml" });
        webbisService = (WebbisService) context.getBean("webbisServiceProxy");
        webbisImageService = (WebbisImageService) context.getBean("webbisImageServiceProxy");
    }

    public void saveWebbis(String sessionId, Webbis webbis) {
        webbisService.save(sessionId, webbis);
    }

    public List<MainWebbisBean> getWebbisarForAuthorId(String userId) {
        List<MainWebbisBean> mainBeans = new ArrayList<MainWebbisBean>();
        List<Webbis> webbisars = webbisService.getWebbisarForAuthorId(userId);
        for (Webbis w : webbisars) {
            mainBeans.add(new MainWebbisBean(w, null));
        }
        return mainBeans;
    }

    public void deleteWebbis(String webbisId) {
        webbisService.delete(Long.parseLong(webbisId));
    }

    public List<Webbis> searchWebbisIncludeDisabled(String searchString, int firstResult, int maxResults) {
        return webbisService.searchWebbisarIncludeDisabled(searchString, firstResult, maxResults);
    }

    public int getNumberOfMatchesForIncludeDisabled(String searchString) {
        return webbisService.getNumberOfMatchesForIncludeDisabled(searchString);
    }

    public void toggleEnableDisable(String webbisId) {
        webbisService.toggleEnableDisable(webbisId);
    }

    public void resize(List<String> imagePaths) {
        webbisImageService.resize(imagePaths);
    }

    public void deleteImages(List<String> toBeDeletedList) {
        webbisImageService.deleteImages(toBeDeletedList);
    }

    public void cleanUpTempDir(String dir) {
        webbisImageService.cleanUpTempDir(dir);
    }

    public Webbis prepareForEditing(String sessionId, String webbisId) {
        return webbisService.prepareForEditing(sessionId, Long.parseLong(webbisId));
    }

    public String getImageBaseUrl() {
        return webbisService.getImageBaseUrl();
    }

    public String getFtpConfig() {
        return webbisService.getFtpConfiguration();
    }

    public Boolean isTestMode() {
        return webbisService.isTestMode();
    }

    public int getMaxNoOfVideoFiles() {
        return webbisService.getMaxNoOfVideoFiles();
    }

    public int getMaxVideoFileSize() {
        return webbisService.getMaxVideoFileSize();
    }

    public void reindex() {
        webbisService.reindex();
    }
}
