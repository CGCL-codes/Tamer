package be.novelfaces.showcase.webdriver.pages.download;

import org.openqa.selenium.By;
import be.novelfaces.showcase.webdriver.pages.NovelFacesPage;

public class DownloadPage extends NovelFacesPage<DownloadPage> {

    public boolean isGoogleCodeDownloadLinkPresent() {
        return isElementPresent(By.cssSelector("a[href='http://code.google.com/p/novelfaces/downloads/list']"));
    }

    @Override
    protected String getContentHeaderTitle() {
        return getProperty("download_cap");
    }

    @Override
    protected void initNovelFacesPage() {
    }
}
