package taseanalyzer.parsers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import taseanalyzer.model.entities.TASE0036Update;
import taseanalyzer.model.entities.Security;
import taseanalyzer.types.YieldTrend;

/**
 *
 * FILE 0036: BONDS - CALCULATED DATA
 *
 * RECORD TYPE 02: DATA
 *
 * +---------------------------------------------------------------------------------------------------+
 * |FIELD|         FIELD NAME        | LENGTH | PICTURE |                    REMARKS                   |
 * |  NO.|                           |        |         |                                              |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  1  |RECORD TYPE                |    2   |9(2)     |VALUE = 02                                    | 0-2
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  2  |SECURITY ID                |    8   |9(8)     |                                              | 2-10
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  3  |PRICE                      |    9   |9(7)V99  |                                              | 10-19
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  4  |FILLER                     |    1   |9        |ZERO                                          | 19-20
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  5  |YIELD COEFFICIENT OF 1%    |    4   |9(2)V9(2)|                                              | 20-24
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  6  |YIELD COEFFICIENT OF 3%    |    4   |9(2)V9(2)|                                              | 24-28
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  7  |YIELD COEFFICIENT OF 6%    |    4   |9(2)V9(2)|                                              | 28-32
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  8  |YIELD FOR PRIOR REDEMPTION |    4   |9(2)V9(2)|                                              | 32-36
 * |     |PATH 1                     |        |         |                                              |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  9  |YIELD TREND                |    1   |9        |PERTAINS TO FIELD 8, 0-NO VALUE, 1- DECLINE,  | 36-37
 * |     |                           |        |         |2 - ADVANCE                                   |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  10 |YIELD FOR PRIOR REDEMPTION |    4   |9(2)V9(2)|                                              | 37-41
 * |     |PATH 2                     |        |         |                                              |
 * |---------------------------------------------------------------------------------------------------|
 * |  11 |YIELD TREND                |    1   |9        |PERTAINS TO FIELD 10, 0- NO VALUE, 1 -DECLINE,| 41-42
 * |     |                           |        |         |2- ADVANCE                                    |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  12 |NET YIELD, PATH 2          |    4   |9(2)V9(2)|                                              | 42-46
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  13 |YIELD TREND                |    1   |9        |PERTAINS TO FIELD 12, 0- NO VALUE, 1 -DECLINE,| 46-47
 * |     |                           |        |         |2- ADVANCE                                    |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  14 |ADJUSTED VALUE (PARI), PATH|    8   |9(7)V9   |LESS THAN 300 ACCURACY TO THE FIRST DIGIT     | 47-55
 * |     |1                          |        |         |AFTER THE DECIMAL POINT                       |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  15 |ADJUSTED VALUE (PARI), PATH|    8   |9(7)V9   |LESS THAN 300 ACCURACY TO THE FIRST DIGIT     | 55-63
 * |     |2                          |        |         |AFTER THE DECIMAL POINT                       |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  16 |FILLER                     |   17   |X(17)    |ZEROES                                        | 63-80
 * +---------------------------------------------------------------------------------------------------+
 *
 * RECORD TYPE 03: ADDITIONAL DATA
 *
 * +---------------------------------------------------------------------------------------------------+
 * |FIELD|         FIELD NAME        | LENGTH | PICTURE |                    REMARKS                   |
 * |  NO.|                           |        |         |                                              |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  1  |RECORD TYPE                |    2   |9(2)     |VALUE = 03                                    | 0-2
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  2  |SECURITY ID                |    8   |9(8)     |                                              | 2-10
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  3  |GROSS YIELD - PATH 1       |    9   |9(3)V9(6)|                                              | 10-19
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  4  |GROSS YIELD TREND          |    1   |9        |PERTAINS TO FIELD 3, 0 - NO VALUE, 1 -DECLINE,| 19-20
 * |     |                           |        |         |2 - ADVANCE                                   |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  5  |RELATIVE GROSS YIELD, PATH |    9   |9(3)V9(6)|                                              | 20-29
 * |     |1                          |        |         |                                              |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  6  |RELATIVE GROSS YIELD TREND |    1   |9        |PERTAINS TO FIELD 5, 0 -NO VALUE, 1 - DECLINE,| 29-30
 * |     |                           |        |         |2 - ADVANCE                                   |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  7  |NET YIELD PATH 1           |    9   |9(3)V9(6)|                                              | 30-39
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  8  |NET YIELD TREND            |    1   |9        |PERTAINS TO FIELD 7, 0 - NO VALUE, - DECLINE, | 39-40
 * |     |                           |        |         |2 - ADVANCE                                   |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  9  |CURRENCY CODE, PATH 1      |    2   |9(2)     |                                              | 40-42
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  10 |CURRENCY CODE, PATH 2      |    2   |9(2)     |                                              | 42-44
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  11 |GROSS YIELD, PATH 2        |    9   |9(3)V9(6)|                                              | 44-53
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  12 |GROSS YIELD TREND          |    1   |9        |PERTAINS TO FIELD 11, 0- NO VALUE, 1-DECLINE, | 53-54
 * |     |                           |        |         |2 - ADVANCE                                   |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  13 |RELATIVE GROSS YIELD, PATH |    9   |9(3)V9(6)|                                              | 54-63
 * |     |2                          |        |         |                                              |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  14 |RELATIVE GROSS YIELD TREND |    1   |9        |PERTAINS TO FIELD 13, 0- NO VALUE, 1 -DECLINE | 63-64
 * |     |                           |        |         |2 - ADVANCE                                   |
 * |-----+---------------------------+--------+---------+----------------------------------------------|
 * |  15 |FILLER                     |   16   |X(16)    |                                              | 64-80
 * +---------------------------------------------------------------------------------------------------+
 *
 * @author Arieh Bibliowicz
 */
public class TASE0036Parser extends TASEFileParser {

    Logger logger = Logger.getLogger(TASE0036Parser.class.getName());

    @Override
    protected void parseSpecific() throws IOException {
        if (true) throw new RuntimeException("Implementation must be updated.");
        Security security;
        TASE0036Update update;
        boolean createdSecurity;
        boolean createdUpdate;
        reader.mark(255);
        String line = reader.readLine();
        String recordType = line.substring(0, 2);
        while (!recordType.equals("99")) {
            security = null;
            update = null;
            createdSecurity = false;
            createdUpdate = false;
            if (recordType.equals("02")) {
                try {
                    String securityID = line.substring(2, 10);
                    String price = line.substring(10, 19);
                    String yieldCoefficientOnePct = line.substring(20, 24);
                    String yieldCoefficientThreePct = line.substring(24, 28);
                    String yieldCoefficientSixPct = line.substring(28, 32);
                    String yieldForPriorRedemptionPathOne = line.substring(32, 36);
                    String yieldForPriorRedemptionPathOneTrend = line.substring(36, 37);
                    String yieldForPriorRedemptionPathTwo = line.substring(37, 41);
                    String yieldForPriorRedemptionPathTwoTrend = line.substring(41, 42);
                    String netYieldPathTwo = line.substring(42, 46);
                    String netYieldPathTwoTrend = line.substring(46, 47);
                    String adjustedValuePathOne = line.substring(47, 55);
                    String adjustedValuePathTwo = line.substring(55, 63);
                    double doublePrice = Double.parseDouble(price);
                    double doubleYieldCoefficientOnePct = Double.parseDouble(yieldCoefficientOnePct);
                    double doubleYieldCoefficientThreePct = Double.parseDouble(yieldCoefficientThreePct);
                    double doubleYieldCoefficientSixPct = Double.parseDouble(yieldCoefficientSixPct);
                    double doubleYieldForPriorRedemptionPathOne = Double.parseDouble(yieldForPriorRedemptionPathOne);
                    int intYieldForPriorRedemptionPathOneTrend = Integer.parseInt(yieldForPriorRedemptionPathOneTrend);
                    double doubleYieldForPriorRedemptionPathTwo = Double.parseDouble(yieldForPriorRedemptionPathTwo);
                    int intYieldForPriorRedemptionPathTwoTrend = Integer.parseInt(yieldForPriorRedemptionPathTwoTrend);
                    double doubleNetYieldPathTwo = Double.parseDouble(netYieldPathTwo);
                    int intNetYieldPathTwoTrend = Integer.parseInt(netYieldPathTwoTrend);
                    double doubleAdjustedValuePathOne = Double.parseDouble(adjustedValuePathOne);
                    double doubleAdjustedValuePathTwo = Double.parseDouble(adjustedValuePathTwo);
                    security = em.fetchSecurityBySecurityID(securityID);
                    if (security == null) {
                        security = new Security();
                        security.setSecurityID(securityID);
                        createdSecurity = true;
                    }
                    update = new TASE0036Update();
                    createdUpdate = true;
                    update.setSecurity(security);
                    update.setUpdateDate(getTASEFileDate());
                    update.setPrice(doublePrice);
                    update.setYieldCoefficientOnePct(doubleYieldCoefficientOnePct);
                    update.setYieldCoefficientThreePct(doubleYieldCoefficientThreePct);
                    update.setYieldCoefficientSixPct(doubleYieldCoefficientSixPct);
                    update.setYieldForPriorRedemptionPathOne(doubleYieldForPriorRedemptionPathOne);
                    update.setYieldForPriorRedemptionPathOneTrend(YieldTrend.codeToEnum(intYieldForPriorRedemptionPathOneTrend));
                    update.setYieldForPriorRedemptionPathTwo(doubleYieldForPriorRedemptionPathTwo);
                    update.setYieldForPriorRedemtionPathTwoTrend(YieldTrend.codeToEnum(intYieldForPriorRedemptionPathTwoTrend));
                    update.setNetYieldPathTwo(doubleNetYieldPathTwo);
                    update.setNetYieldPathTwoTrend(YieldTrend.codeToEnum(intNetYieldPathTwoTrend));
                    update.setAdjustedValuePathOne(doubleAdjustedValuePathOne);
                    update.setAdjustedValuePathTwo(doubleAdjustedValuePathTwo);
                } catch (Exception e) {
                    logger.severe("Invalid record: " + line);
                    logger.log(Level.SEVERE, "Exception: ", e);
                    createdSecurity = false;
                    createdUpdate = false;
                }
                if (createdSecurity) {
                    createdEntities.add(security);
                }
                if (createdUpdate) {
                    createdEntities.add(update);
                }
            } else if (recordType.equals("03")) {
                logger.fine("Record " + recordType + " not processed.");
            } else {
                logger.severe("Found invalid record type: " + recordType);
            }
            reader.mark(255);
            line = reader.readLine();
            recordType = line.substring(0, 2);
        }
        reader.reset();
    }
}
