package sk.sigp.tetras.findemail;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import sk.sigp.tetras.dao.EmailDao;
import sk.sigp.tetras.dao.FirmaDao;
import sk.sigp.tetras.dao.TrafficDao;
import sk.sigp.tetras.entity.Email;
import sk.sigp.tetras.entity.Firma;
import sk.sigp.tetras.entity.Traffic;
import sk.sigp.tetras.findemail.crawl.CrawlerHttpClient;
import sk.sigp.tetras.findemail.google.GoogleSearchWrapper;
import sk.sigp.tetras.service.PreferenceService;

/**
 * manager responsible for unsubscribing via email
 * 
 * @author mathew
 * 
 */
public class FindEmailManager {

    public static String EMAIL_REGEXP = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";

    private static Logger LOG = Logger.getLogger(FindEmailManager.class);

    private EmailDao emailDao;

    private FirmaDao firmaDao;

    private TrafficDao trafficDao;

    private CrawlerHttpClient httpClient;

    private GoogleSearchWrapper googleSearchWrapper;

    private PreferenceService preferenceService;

    /**
	 * main iteration method
	 */
    public void process() {
        double suceeded = 0;
        double count = 0;
        try {
            LOG.info("START of Find emails process iteration");
            List<Firma> companies = getFirmaDao().findSomeForFindingEmails(getPreferenceService().getFindemailCompaniesPerIteration());
            for (Firma company : companies) {
                company.setDontFindEmails(true);
                getFirmaDao().update(company);
            }
            LOG.info("Companies for interation were updated so they never get thru this procedure again");
            count = getPreferenceService().getFindemailCompaniesPerIteration();
            for (Firma company : companies) if (processCompany(company)) suceeded++;
        } catch (Exception error) {
            LOG.error("Problem during find emails iteration", error);
        } finally {
            double ratio = suceeded / count * 100;
            LOG.info("Successful FINISH Find emails process iteration with effectiveness " + ratio + "%");
        }
    }

    /**
	 * single company processing
	 * @param company
	 */
    public boolean processCompany(Firma company) {
        LOG.info("Processing company ID: " + company.getId() + ", Name: " + company.getFirmaData().getName());
        List<String> googleSearchResults = getGoogleSearchWrapper().makeQuery(company.getFirmaData().getName());
        boolean found = false;
        int desiredCount = getPreferenceService().getFindemailPagesForCompany();
        String[] whatWeDontWant = getPreferenceService().getFindemailIgnoreUrls().split(",");
        List<String> preprocessed = new ArrayList<String>();
        for (String str : googleSearchResults) {
            boolean good = true;
            for (String tx : whatWeDontWant) if (str.toLowerCase().contains(tx.toLowerCase())) {
                good = false;
                break;
            }
            if (good) preprocessed.add(str);
        }
        List<String> finalDesiredResults = desiredCount >= preprocessed.size() ? preprocessed : preprocessed.subList(0, desiredCount);
        for (String luck : finalDesiredResults) {
            try {
                if (luck.toLowerCase().endsWith(".pdf")) continue;
                String luckData = getHttpClient().httpToStringStupid(luck);
                Traffic traffic = new Traffic();
                traffic.setDetail(luck);
                traffic.setDescription("FindEmailManager, fetching google results");
                traffic.setSize(Long.valueOf(luckData.length()));
                getTrafficDao().save(traffic);
                Pattern emailPattern = Pattern.compile(EMAIL_REGEXP);
                Matcher emailMatcher = emailPattern.matcher(luckData);
                int max = 0;
                while (emailMatcher.find()) {
                    if (max > getPreferenceService().getFindemailMaxEmailsForCompany()) break;
                    String email = emailMatcher.group();
                    Email testEmail = getEmailDao().findEmailsByEmail(email.toLowerCase().trim());
                    if (testEmail == null) {
                        LOG.info("Found:" + email);
                        Email storeEmail = new Email();
                        storeEmail.setEmail(email);
                        storeEmail.setFirma(company);
                        storeEmail.setAuto(true);
                        getEmailDao().save(storeEmail);
                        found = true;
                        max++;
                    }
                }
                if (found) break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return found;
    }

    public EmailDao getEmailDao() {
        return emailDao;
    }

    public void setEmailDao(EmailDao emailDao) {
        this.emailDao = emailDao;
    }

    public CrawlerHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CrawlerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public FirmaDao getFirmaDao() {
        return firmaDao;
    }

    public void setFirmaDao(FirmaDao firmaDao) {
        this.firmaDao = firmaDao;
    }

    public GoogleSearchWrapper getGoogleSearchWrapper() {
        return googleSearchWrapper;
    }

    public void setGoogleSearchWrapper(GoogleSearchWrapper googleSearchWrapper) {
        this.googleSearchWrapper = googleSearchWrapper;
    }

    public PreferenceService getPreferenceService() {
        return preferenceService;
    }

    public void setPreferenceService(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    public TrafficDao getTrafficDao() {
        return trafficDao;
    }

    public void setTrafficDao(TrafficDao trafficDao) {
        this.trafficDao = trafficDao;
    }
}
