package ad.library.android.view;

import net.youmi.android.appoffers.AppOffersManager;
import org.sharp.android.autils.AIOUtils;
import org.sharp.android.base.BaseActiveSensor;
import org.sharp.android.base.BasePlugin;
import org.sharp.android.intf.ActiveSensor;
import org.sharp.android.intf.MenuOperation;
import org.sharp.android.intf.Plugin;
import org.sharp.android.view.ViewUtils;
import org.sharp.intf.PointsSupport;
import org.sharp.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class PluggableUtils {

    public static ActiveSensor newAdModAd(final Activity act, final View p, final int adFrameResourceId) {
        return new BaseActiveSensor() {

            private com.google.ads.AdView adView;

            @Override
            public void onPause() {
                adView.destroy();
            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                adView = new com.google.ads.AdView(act, com.google.ads.AdSize.IAB_BANNER, "a14dd0e63d68392");
                com.google.ads.AdRequest adRequest = new com.google.ads.AdRequest();
                adRequest.addTestDevice(adRequest.TEST_EMULATOR);
                adRequest.addKeyword("日语");
                ViewGroup vg = (ViewGroup) p.findViewById(adFrameResourceId);
                vg.addView(adView);
                adView.loadAd(adRequest);
            }
        };
    }

    public static class YoumiAd implements ActiveSensor {

        Activity act;

        View p;

        int adFrameResourceId;

        static {
            boolean testMode = false;
            net.youmi.android.AdManager.init("ffe7bcfd7331f676", "e3c78d6786dff52d", 30, testMode);
        }

        public YoumiAd(final Activity act, final View p, final int adFrameResourceId) {
            this.act = act;
            this.p = p;
            this.adFrameResourceId = adFrameResourceId;
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            ViewGroup vg = (ViewGroup) p.findViewById(adFrameResourceId);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup youmiAd = (ViewGroup) inflater.inflate(ad.library.R.layout.youmi_ad, null);
            View youmiAdView = youmiAd.findViewById(ad.library.R.id.youmiAdView);
            youmiAd.removeView(youmiAdView);
            vg.addView(youmiAdView);
        }
    }

    public static ActiveSensor newYoumiAd(final Activity act, final View p, int adFrameResourceId) {
        return new YoumiAd(act, p, adFrameResourceId);
    }

    public static class YoumiAppOffers extends BasePlugin implements PointsSupport {

        protected static final int MENU_EARN_POINTS = Utils.uniqueInt();

        Activity mActi;

        private boolean mEarnPointsConfirmed = false;

        public YoumiAppOffers(Activity acti) {
            mActi = acti;
        }

        @Override
        public boolean checkPoints(int bonus) {
            AIOUtils.log("about to spend 1 point.");
            spendPoints(1);
            int points = getPoints();
            if ((points + bonus) <= 0) {
                if (!mEarnPointsConfirmed) {
                    earnPoints();
                } else {
                }
                return false;
            } else {
                AIOUtils.log(points + " points left.");
                return true;
            }
        }

        @Override
        public int getPoints() {
            return AppOffersManager.getPoints(mActi);
        }

        @Override
        public boolean spendPoints(int amount) {
            return AppOffersManager.spendPoints(mActi, amount);
        }

        @Override
        public void earnPoints() {
            ViewUtils.showDialog(mActi, mActi.getString(ad.library.R.string.dlg_title_insufficient_points), mActi.getString(ad.library.R.string.dlg_msg_prompt_make_points), mActi.getString(ad.library.R.string.dlg_button_earn_now), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface di, int id) {
                    AppOffersManager.showAppOffers(mActi);
                }
            }, mActi.getString(ad.library.R.string.dlg_button_earn_later), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface di, int id) {
                    di.cancel();
                }
            });
        }

        @Override
        public void hintNoPoints() {
            ViewUtils.showToast(mActi, ad.library.R.string.toast_make_points_to_play_sound, Toast.LENGTH_SHORT);
        }

        @Override
        public ActiveSensor activeSensor() {
            return new BaseActiveSensor() {

                @Override
                public void onCreate(Bundle savedInstanceState) {
                    boolean testmode = false;
                    AppOffersManager.init(mActi, "ffe7bcfd7331f676", "e3c78d6786dff52d", testmode);
                }
            };
        }

        @Override
        public MenuOperation menuOperation() {
            return new MenuOperation() {

                @Override
                public boolean onCreateOptionsMenu(Menu menu) {
                    menu.add(0, MENU_EARN_POINTS, 0, mActi.getString(ad.library.R.string.menu_earn_points));
                    return true;
                }

                @Override
                public boolean onOptionsItemSelected(MenuItem item) {
                    if (item.getItemId() == MENU_EARN_POINTS) {
                        AppOffersManager.showAppOffers(mActi);
                        return true;
                    } else {
                        return false;
                    }
                }
            };
        }
    }

    private static YoumiAppOffers youmiAppOffers;

    public static YoumiAppOffers youmiAppOffers(final Activity act) {
        if (youmiAppOffers == null) {
            youmiAppOffers = new YoumiAppOffers(act);
        } else {
            youmiAppOffers.mActi = act;
        }
        return youmiAppOffers;
    }

    public static Plugin youmiAppOffersPlugin(final Activity act) {
        return youmiAppOffers(act);
    }

    public static PointsSupport pointsSupport(final Activity act) {
        return youmiAppOffers(act);
    }
}
