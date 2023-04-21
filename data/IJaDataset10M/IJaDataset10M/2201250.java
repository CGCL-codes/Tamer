package dk.bot.betfairservice;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import com.betfair.publicapi.types.exchange.v5.BetStatusEnum;
import com.betfair.publicapi.types.exchange.v5.MUBet;
import dk.bot.betfairservice.model.BFMarketData;
import dk.bot.betfairservice.model.BFMarketRunner;
import dk.bot.betfairservice.model.BFMarketRunners;

public class BetFairUtilTest {

    @Test
    public void testParseMarketsDataOk() throws BetFairException {
        String marketData = " :20158165~Match Odds~O~ACTIVE~1164223800000~" + "\\Soccer\\Scottish Soccer\\Bells League Div 1\\Fixtures 22 November \\Partick v Clyde" + "~/1/2695886/610072/10551708/10551709/20158165~0~1~GBR~1164192924479~3~1~8737.44~Y~Y:";
        List<BFMarketData> markets = BetFairUtil.parseMarketsData(marketData);
        assertEquals(1, markets.size());
        BFMarketData market = markets.get(0);
        assertEquals(20158165, market.getMarketId());
        assertEquals("Match Odds", market.getMarketName());
        assertEquals("O", market.getMarketType());
        assertEquals("ACTIVE", market.getMarketStatus());
        assertEquals(1164223800000l, market.getEventDate().getTime());
        assertEquals("\\Soccer\\Scottish Soccer\\Bells League Div 1\\Fixtures 22 November \\Partick v Clyde", market.getMenuPath());
        assertEquals("/1/2695886/610072/10551708/10551709/20158165", market.getEventHierarchy());
        assertEquals("0", market.getBetDelay());
        assertEquals(1, market.getExchangeId());
        assertEquals("GBR", market.getCountryCode());
        assertEquals(1164192924479l, market.getLastRefresh().getTime());
        assertEquals(3, market.getNumberOfRunners());
        assertEquals(1, market.getNumberOfWinners());
        assertEquals(8737.44d, market.getTotalAmountMatched(), 0);
        assertEquals(true, market.isBsbMarket());
        assertEquals(true, market.isTurningInPlay());
    }

    @Test
    public void testParseMarketPrices() {
        String marketPrices = "20771785~5~:58805~3~11510.72~4.1~~~false~~~~~|1.01~673.36~0.0~0.0~0.0~1.02~6.75~0.0~" + "0.0~0.0~1.05~2.25~0.0~0.0~0.0~1.1~1.5~0.0~0.0~0.0~1.15~0.75~0.0~0.0~0.0~2.74~11.49~0" + ".0~0.0~0.0~3.05~9.76~0.0~0.0~0.0~3.2~22.0~0.0~0.0~0.0~3.25~22.0~0.0~0.0~0.0~3.3~10.5" + "~0.0~0.0~0.0~3.4~3.75~0.0~0.0~0.0~3.45~19.61~0.0~0.0~0.0~3.5~148.44~0.0~0.0~0.0~3.55" + "~200.07~0.0~0.0~0.0~3.6~572.77~0.0~0.0~0.0~3.65~407.5~0.0~0.0~0.0~3.7~908.44~0.0~0.0" + "~0.0~3.75~42.18~0.0~0.0~0.0~3.8~36.32~0.0~0.0~0.0~3.85~218.56~0.0~0.0~0.0~3.9~368.05" + "~0.0~0.0~0.0~3.95~190.42~0.0~0.0~0.0~4.0~643.01~0.0~0.0~0.0~4.1~0.0~66.53~0.0~0.0~4." + "2~0.0~366.57~0.0~0.0~4.3~0.0~9.26~0.0~0.0~4.4~0.0~83.59~0.0~0.0~4.6~0.0~3.0~0.0~0.0~" + "4.8~0.0~3.0~0.0~0.0~5.1~0.0~2.66~0.0~0.0~1000.0~0.0~3.75~0.0~0.0~:214217~1~134754.06" + "~1.64~~~false~~~~|1.01~679.2~0.0~0.0~0.0~1.02~3.0~0.0~0.0~0.0~1.05~2.25~0.0~0.0~0.0~" + "1.1~1.5~0.0~0.0~0.0~1.15~0.75~0.0~0.0~0.0~1.21~95.24~0.0~0.0~0.0~1.35~57.14~0.0~0.0~" + "0.0~1.49~3.0~0.0~0.0~0.0~1.5~1.5~0.0~0.0~0.0~1.51~75.03~0.0~0.0~0.0~1.53~3.0~0.0~0.0" + "~0.0~1.54~287.78~0.0~0.0~0.0~1.56~373.03~0.0~0.0~0.0~1.57~2116.73~0.0~0.0~0.0~1.59~2" + ".0~0.0~0.0~0.0~1.6~0.0~0.0~0.0~0.0~1.61~235.77~0.0~0.0~0.0~1.62~174.01~0.0~0.0~0.0~1" + ".63~497.31~0.0~0.0~0.0~1.64~83.91~0.0~0.0~0.0~1.65~0.0~1898.95~0.0~0.0~1.66~0.0~632." + "86~0.0~0.0~1.67~0.0~786.56~0.0~0.0~1.68~0.0~1378.7~0.0~0.0~1.69~0.0~153.38~0.0~0.0~1" + ".7~0.0~10322.87~0.0~0.0~1.71~0.0~1852.29~0.0~0.0~1.72~0.0~27753.86~0.0~0.0~1.73~0.0~" + "75.03~0.0~0.0~1.74~0.0~502.76~0.0~0.0~1.75~0.0~18.0~0.0~0.0~1.76~0.0~39.45~0.0~0.0~1" + ".77~0.0~1326.81~0.0~0.0~1.78~0.0~7.33~0.0~0.0~1.79~0.0~0.0~0.0~0.0~1.8~0.0~532.37~0." + "0~0.0~1.87~0.0~511.29~0.0~0.0~3.0~0.0~7.5~0.0~0.0~1000.0~0.0~3.0~0.0~0.0~:13362~2~10" + "476.68~6.8~~~false~~~~|1.01~676.1~0.0~0.0~0.0~1.02~3.0~0.0~0.0~0.0~1.05~2.25~0.0~0.0" + "~0.0~1.1~1.5~0.0~0.0~0.0~1.15~0.75~0.0~0.0~0.0~4.4~0.0~0.0~0.0~0.0~4.7~5.41~0.0~0.0~" + "0.0~5.3~7.65~0.0~0.0~0.0~5.4~42.07~0.0~0.0~0.0~5.5~89.99~0.0~0.0~0.0~5.6~150.14~0.0~" + "0.0~0.0~6.0~98.57~0.0~0.0~0.0~6.2~321.02~0.0~0.0~0.0~6.4~296.48~0.0~0.0~0.0~6.6~215." + "87~0.0~0.0~0.0~6.8~128.17~0.0~0.0~0.0~7.0~0.0~25.09~0.0~0.0~7.2~0.0~334.74~0.0~0.0~7" + ".4~0.0~288.24~0.0~0.0~7.6~0.0~0.0~0.0~0.0~7.8~0.0~2.0~0.0~0.0~8.4~0.0~2.0~0.0~0.0~8." + "6~0.0~9.0~0.0~0.0~";
        BFMarketRunners marketRunners = BetFairUtil.parseMarketPrices(marketPrices, new Date(100));
        assertEquals(20771785, marketRunners.getMarketId());
        assertEquals(5, marketRunners.getInPlayDelay());
        assertEquals(100, marketRunners.getTimestamp().getTime());
        List<BFMarketRunner> runners = marketRunners.getMarketRunners();
        assertEquals(3, runners.size());
        assertEquals(60253.04, marketRunners.getTotalToBet(), 0);
        assertEquals(58805, runners.get(0).getSelectionId());
        assertEquals(11510.72d, runners.get(0).getTotalAmountMatched(), 0);
        assertEquals(4.1d, runners.get(0).getLastPriceMatched(), 0);
        assertEquals(4.0d, runners.get(0).getPriceToBack(), 0);
        assertEquals(214217, runners.get(1).getSelectionId());
        assertEquals(134754.06d, runners.get(1).getTotalAmountMatched(), 0);
        assertEquals(1.64d, runners.get(1).getLastPriceMatched(), 0);
        assertEquals(1.64d, runners.get(1).getPriceToBack(), 0);
        assertEquals(13362, runners.get(2).getSelectionId());
        assertEquals(10476.68d, runners.get(2).getTotalAmountMatched(), 0);
        assertEquals(6.8d, runners.get(2).getLastPriceMatched(), 0);
        assertEquals(1.01d, runners.get(2).getPrices().get(0).getPrice(), 0);
        assertEquals(676.1d, runners.get(2).getPrices().get(0).getTotalToBack(), 0);
        assertEquals(0d, runners.get(2).getPrices().get(0).getTotalToLay(), 0);
        assertEquals(1.02d, runners.get(2).getPrices().get(1).getPrice(), 0);
        assertEquals(3.0d, runners.get(2).getPrices().get(1).getTotalToBack(), 0);
        assertEquals(0d, runners.get(2).getPrices().get(1).getTotalToLay(), 0);
        assertEquals(5.6d, runners.get(2).getPrices().get(10).getPrice(), 0);
        assertEquals(150.14d, runners.get(2).getPrices().get(10).getTotalToBack(), 0);
        assertEquals(0d, runners.get(2).getPrices().get(10).getTotalToLay(), 0);
        assertEquals(6.8d, runners.get(2).getPriceToBack(), 0);
    }

    @Test
    public void testParseMarketPricesNoRunnerPrices() {
        String marketPrices = "100423584~0~:2521895~4~0.0~~~~false~0~~~~|:2521916~25~0.0~~~~false~0~~~~|:2521903~27~0.0~~~~false~0~~~~|:2521919~34~0.0~~~~false~0~~~~|:2522765~30~0.0~~~~false~0~~~~|:2521898~22~0.0~~~~false~0~~~~|:2521911~31~0.0~~~~false~0~~~~|:2522768~35~0.0~~~~false~0~~~~|:2521893~10~0.0~~~~false~0~~~~|:2521897~17~0.0~~~~false~0~~~~|:2522766~33~0.0~~~~false~0~~~~|:2521904~6~0.0~~~~false~0~~~~|:2521914~14~0.0~~~~false~0~~~~|:2521896~11~0.0~~~~false~0~~~~|:2521905~13~0.0~~~~false~0~~~~|:2521890~2~0.0~~~~false~0~~~~|:2521913~7~0.0~~~~false~0~~~~|:2521899~5~0.0~~~~false~0~~~~|:2522769~36~0.0~~~~false~0~~~~|:2521910~28~0.0~~~~false~0~~~~|:2522764~26~0.0~~~~false~0~~~~|:2521891~9~0.0~~~~false~0~~~~|:2521892~3~0.0~~~~false~0~~~~|:2522761~8~0.0~~~~false~0~~~~|:2521915~20~0.0~~~~false~0~~~~|:2521902~23~0.0~~~~false~0~~~~|:2521908~19~0.0~~~~false~0~~~~|:2521918~32~0.0~~~~false~0~~~~|:2521901~18~0.0~~~~false~0~~~~|:2521889~1~0.0~~~~false~0~~~~|:2522762~15~0.0~~~~false~0~~~~|:2521909~24~0.0~~~~false~0~~~~|:2521894~16~0.0~~~~false~0~~~~|:2521900~12~0.0~~~~false~0~~~~|:2521917~29~0.0~~~~false~0~~~~|:2522763~21~0.0~~~~false~0~~~~|";
        BFMarketRunners marketRunners = BetFairUtil.parseMarketPrices(marketPrices, new Date(100));
        assertEquals(36, marketRunners.getMarketRunners().size());
        assertEquals(0, marketRunners.getTotalToBet(), 0);
    }

    @Test
    public void testMergBetsCheckPlacedDate() throws DatatypeConfigurationException {
        ArrayList<MUBet> bets = new ArrayList<MUBet>();
        MUBet mBet1 = new MUBet();
        XMLGregorianCalendar xmlCalFrom = DatatypeFactory.newInstance().newXMLGregorianCalendar(2000, 1, 1, 1, 1, 1, 1, 1);
        mBet1.setMatchedDate(xmlCalFrom);
        mBet1.setBetStatus(BetStatusEnum.M);
        bets.add(mBet1);
        MUBet mBet2 = new MUBet();
        xmlCalFrom = DatatypeFactory.newInstance().newXMLGregorianCalendar(2012, 1, 1, 1, 1, 1, 1, 1);
        mBet2.setMatchedDate(xmlCalFrom);
        mBet2.setBetStatus(BetStatusEnum.M);
        bets.add(mBet2);
        MUBet mBet3 = new MUBet();
        xmlCalFrom = DatatypeFactory.newInstance().newXMLGregorianCalendar(2005, 1, 1, 1, 1, 1, 1, 1);
        mBet3.setMatchedDate(xmlCalFrom);
        mBet3.setBetStatus(BetStatusEnum.M);
        bets.add(mBet3);
        List<MUBet> mergedBets = BetFairUtil.mergBets(bets);
        assertEquals(1325376061001l, mergedBets.get(0).getMatchedDate().toGregorianCalendar().getTime().getTime());
    }

    @Test
    public void tesValidatePrice() {
        List<PriceRange> priceRanges = new ArrayList<PriceRange>();
        PriceRange pr1 = new PriceRange(1.01, 2.0, 0.01);
        PriceRange pr2 = new PriceRange(2.0, 3.0, 0.02);
        PriceRange pr3 = new PriceRange(3.0, 4.0, 0.05);
        PriceRange pr4 = new PriceRange(4.0, 6.0, 0.1);
        PriceRange pr5 = new PriceRange(6.0, 10.0, 0.2);
        PriceRange pr6 = new PriceRange(10.0, 20.0, 0.5);
        PriceRange pr7 = new PriceRange(20.0, 30.0, 1.0);
        PriceRange pr8 = new PriceRange(30.0, 50.0, 2.0);
        PriceRange pr9 = new PriceRange(50.0, 100.0, 5.0);
        PriceRange pr10 = new PriceRange(100.0, 1000.0, 10.0);
        priceRanges.add(pr1);
        priceRanges.add(pr2);
        priceRanges.add(pr3);
        priceRanges.add(pr4);
        priceRanges.add(pr5);
        priceRanges.add(pr6);
        priceRanges.add(pr7);
        priceRanges.add(pr8);
        priceRanges.add(pr9);
        priceRanges.add(pr10);
        assertEquals(1.64, BetFairUtil.validatePrice(priceRanges, 1.6317991631799162, BetFairUtil.ROUND_UP), 0);
        assertEquals(1.63, BetFairUtil.validatePrice(priceRanges, 1.6317991631799162, BetFairUtil.ROUND_DOWN), 0);
        assertEquals(1.88, BetFairUtil.validatePrice(priceRanges, 1.88, BetFairUtil.ROUND_UP), 0);
        assertEquals(1.88, BetFairUtil.validatePrice(priceRanges, 1.88, BetFairUtil.ROUND_DOWN), 0);
        assertEquals(1.01, BetFairUtil.validatePrice(priceRanges, 0.5, BetFairUtil.ROUND_UP), 0);
        assertEquals(1.01, BetFairUtil.validatePrice(priceRanges, 0.5, BetFairUtil.ROUND_DOWN), 0);
        assertEquals(1000, BetFairUtil.validatePrice(priceRanges, 2000, BetFairUtil.ROUND_UP), 0);
        assertEquals(1000, BetFairUtil.validatePrice(priceRanges, 2000, BetFairUtil.ROUND_DOWN), 0);
        assertEquals(1.01, BetFairUtil.validatePrice(priceRanges, 1.01, BetFairUtil.ROUND_UP), 0);
        assertEquals(1.01, BetFairUtil.validatePrice(priceRanges, 1.01, BetFairUtil.ROUND_DOWN), 0);
        assertEquals(1000, BetFairUtil.validatePrice(priceRanges, 1000, BetFairUtil.ROUND_UP), 0);
        assertEquals(1000, BetFairUtil.validatePrice(priceRanges, 1000, BetFairUtil.ROUND_DOWN), 0);
        assertEquals(2.0, BetFairUtil.validatePrice(priceRanges, 2.0, BetFairUtil.ROUND_UP), 0);
        assertEquals(2.0, BetFairUtil.validatePrice(priceRanges, 2.0, BetFairUtil.ROUND_DOWN), 0);
        assertEquals(2.02, BetFairUtil.validatePrice(priceRanges, 2.01, BetFairUtil.ROUND_UP), 0);
        assertEquals(2.0, BetFairUtil.validatePrice(priceRanges, 2.01, BetFairUtil.ROUND_DOWN), 0);
        assertEquals(44, BetFairUtil.validatePrice(priceRanges, 43, BetFairUtil.ROUND_UP), 0);
        assertEquals(44, BetFairUtil.validatePrice(priceRanges, 44, BetFairUtil.ROUND_UP), 0);
        assertEquals(46, BetFairUtil.validatePrice(priceRanges, 45, BetFairUtil.ROUND_UP), 0);
        assertEquals(860, BetFairUtil.validatePrice(priceRanges, 856, BetFairUtil.ROUND_UP), 0);
        assertEquals(1000, BetFairUtil.validatePrice(priceRanges, 999.99, BetFairUtil.ROUND_UP), 0);
        assertEquals(990, BetFairUtil.validatePrice(priceRanges, 999.99, BetFairUtil.ROUND_DOWN), 0);
    }

    @Test
    public void testGetAllPricesForPriceRanges() {
        List<Double> allPricesForPriceRanges = BetFairUtil.getAllPricesForPriceRanges(BetFairUtil.getPriceRanges());
        assertEquals(347, allPricesForPriceRanges.size());
    }
}
