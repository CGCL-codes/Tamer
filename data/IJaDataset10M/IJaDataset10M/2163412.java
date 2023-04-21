package uk.co.massycat.appreviewsfinder.reviews;

import uk.co.massycat.appreviewsfinder.countries.FromCountriesDownloader;
import uk.co.massycat.appreviewsfinder.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import uk.co.massycat.appreviewsfinder.countries.CountriesManager;

/**
 *
 * @author ben
 */
public class ReviewsDownloader extends FromCountriesDownloader {

    static final int kReviewsPerFile = 200;

    private File AppDir;

    private int mAppCode;

    private int mTotalReviews;

    private boolean mLatestOnly;

    class CountReviewsPair {

        public List<AppReview> mReviews;

        public float mTotalRating;

        public int mTotalReviewsCount;

        public int mFilesCount;

        public CountReviewsPair() {
            mFilesCount = 0;
            mTotalRating = 0.f;
            mTotalReviewsCount = 0;
            mReviews = new LinkedList<AppReview>();
        }
    }

    public int getCurrentTotal() {
        return mTotalReviews;
    }

    public ReviewsDownloader(Set<String> countries, File app_dir, int app_code, boolean latest_only) {
        super(countries);
        AppDir = app_dir;
        mAppCode = app_code;
        mLatestOnly = latest_only;
    }

    private void saveOutReviews(CountReviewsPair version_info, File out_file) {
        Iterator<AppReview> reviews_iter = version_info.mReviews.iterator();
        StringBuffer reviews_xml = new StringBuffer();
        AppReviewXMLHandler.startReviewsXml(reviews_xml);
        while (reviews_iter.hasNext()) {
            AppReview review = reviews_iter.next();
            AppReviewXMLHandler.addReviewToXml(review, reviews_xml);
        }
        AppReviewXMLHandler.endReviewsXml(reviews_xml);
        if (reviews_xml != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out_file), "UTF8"));
                writer.write(reviews_xml.toString());
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    protected void doWorkForCountry() {
        Hashtable<String, ReviewsDownloader.CountReviewsPair> reviews_by_version = new Hashtable<String, ReviewsDownloader.CountReviewsPair>();
        for (int page_num = 0; !mCauseExit; page_num++) {
            String http_request = "http://phobos.apple.com/WebObjects/MZStore.woa/wa/viewContentsUserReviews?sortOrdering=4&onlyLatestVersion=" + (mLatestOnly ? "true" : "false") + "&sortAscending=true&pageNumber=" + page_num + "&type=Purple+Software&id=" + mAppCode;
            int itunes_code = CountriesManager.getManager().getITunesCodeForCountry(mCurrentCode);
            String iTunesCode = Integer.toString(itunes_code) + "-1";
            String review_string = Utilities.connectAndGetResponse(http_request, iTunesCode);
            AppReviewDecoder decoder = new AppReviewDecoder(review_string);
            List<AppReview> reviews = decoder.getReviews();
            mTotalReviews += reviews.size();
            if (reviews.size() == 0) {
                break;
            }
            Iterator<AppReview> iterator = reviews.iterator();
            while (iterator.hasNext()) {
                AppReview review = iterator.next();
                CountReviewsPair version_info = reviews_by_version.get(review.mVersion);
                if (version_info == null) {
                    version_info = new CountReviewsPair();
                    reviews_by_version.put(review.mVersion, version_info);
                    File version_dir = new File(AppDir, review.mVersion);
                    if (version_dir.exists()) {
                        File[] review_files = AppReviewsUtils.getReviewFilesForCountryInDirectory(mCurrentCode, version_dir);
                        for (int i = 0; i < review_files.length; i++) {
                            review_files[i].delete();
                        }
                    }
                }
                version_info.mReviews.add(review);
                version_info.mTotalReviewsCount += 1;
                version_info.mTotalRating += review.mRatings;
                if (version_info.mReviews.size() >= kReviewsPerFile) {
                    File version_dir = new File(AppDir, review.mVersion);
                    if (!version_dir.exists()) {
                        version_dir.mkdir();
                    }
                    String file_name = AppReviewsUtils.makeReviewsFilename(mCurrentCode, version_info.mFilesCount);
                    File save_file = new File(version_dir, file_name);
                    saveOutReviews(version_info, save_file);
                    version_info.mFilesCount += 1;
                    version_info.mReviews.clear();
                }
            }
        }
        Set<String> versions = reviews_by_version.keySet();
        Iterator<String> version_iter = versions.iterator();
        while (version_iter.hasNext()) {
            String version = version_iter.next();
            File version_dir = new File(AppDir, version);
            if (!version_dir.exists()) {
                version_dir.mkdir();
            }
            CountReviewsPair version_info = reviews_by_version.get(version);
            if (version_info.mReviews.size() > 0) {
                String file_name = AppReviewsUtils.makeReviewsFilename(mCurrentCode, version_info.mFilesCount);
                File save_file = new File(version_dir, file_name);
                saveOutReviews(version_info, save_file);
            }
            try {
                File file = new File(version_dir, mCurrentCode + AppReviewsUtils.kAppReviewsCountsSuffix);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
                writer.write(Integer.toString(version_info.mTotalReviewsCount) + "\n");
                writer.write(Float.toString(version_info.mTotalRating / (float) version_info.mTotalReviewsCount) + "\n");
                writer.close();
            } catch (Exception e) {
            }
        }
        reviews_by_version = null;
    }
}
