package org.shestkoff.nimium.integration.tws;

import gnu.trove.TLongObjectHashMap;

/**
 * Project: Maxifier
 * User: Vasily
 * Date: 31.10.2009 14:13:39
 * <p/>
 * Copyright (c) 1999-2006 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author Shestkov Vasily
 */
final class ErrorCodes {

    private static final TLongObjectHashMap<ErrorSection> errorSections = new TLongObjectHashMap<ErrorSection>();

    private static final TLongObjectHashMap<String> errorDescriptions = new TLongObjectHashMap<String>();

    static {
        initError(399, ErrorSection.ORDER, "Order message error");
        initError(202, ErrorSection.ORDER, "Order cancelled - Reason:");
        initError(103, ErrorSection.PENDING_ORDER, "Duplicate order ID.");
        initError(135, ErrorSection.DELETING_ORDER, "Can't find order with ID =");
        initError(504, ErrorSection.CONNECTION, "Not connected.");
        initError(502, ErrorSection.CONNECTION, "Couldn't connect to TWS. Confirm that API is enabled in TWS via the Configure>API menu command.");
        initError(1100, ErrorSection.CONNECTION, "Connectivity between IB and TWS has been lost.");
        initError(1300, ErrorSection.CONNECTION, "TWS socket port has been reset and this connection is being dropped. Please reconnect on the new port - <port_num>");
        initError(2110, ErrorSection.CONNECTION, "Connectivity between TWS and server is broken. It will be restored automatically.");
        initError(1101, ErrorSection.CONNECTION, "Connectivity between IB and TWS has been restored- data lost.*");
        initError(1102, ErrorSection.CONNECTION, "Connectivity between IB and TWS has been restored- data maintained.");
        errorDescriptions.put(100, "Max rate of messages per second has been exceeded.");
        errorDescriptions.put(101, "Max number of tickers has been reached.");
        errorDescriptions.put(102, "Duplicate ticker ID.");
        errorDescriptions.put(104, "Can't modify a filled order.");
        errorDescriptions.put(105, "Order being modified does not match original order.");
        errorDescriptions.put(106, "Can't transmit order ID:");
        errorDescriptions.put(107, "Cannot transmit incomplete order.");
        errorDescriptions.put(109, "Price is out of the range defined by the Percentage setting at order defaults frame. The order will not be transmitted.");
        errorDescriptions.put(110, "The price does not conform to the minimum price variation for this security.");
        errorDescriptions.put(111, "The TIF (Tif type) and the order type are incompatible.");
        errorDescriptions.put(113, "The Tif option should be set to DAY for MOC and LOC orders.");
        errorDescriptions.put(114, "Relative orders are valid for stocks only.");
        errorDescriptions.put(115, "Relative orders for US stocks can only be submitted to SMART, SMART_ECN, INSTINET, or PRIMEX.");
        errorDescriptions.put(116, "The order cannot be transmitted to a dead exchange.");
        errorDescriptions.put(117, "The block order size must be at least 50.");
        errorDescriptions.put(118, "VWAP orders must be routed through the VWAP exchange.");
        errorDescriptions.put(119, "Only VWAP orders may be placed on the VWAP exchange.");
        errorDescriptions.put(120, "It is too late to place a VWAP order for today.");
        errorDescriptions.put(121, "Invalid BD flag for the order. Check 'Destination' and 'BD' flag.");
        errorDescriptions.put(122, "No request tag has been found for order:");
        errorDescriptions.put(123, "No record is available for conid:");
        errorDescriptions.put(124, "No market rule is available for conid:");
        errorDescriptions.put(125, "Buy price must be the same as the best asking price.");
        errorDescriptions.put(126, "Sell price must be the same as the best bidding price.");
        errorDescriptions.put(129, "VWAP orders must be submitted at least three minutes before the start time.");
        errorDescriptions.put(131, "The sweep-to-fill flag and display size are only valid for US stocks routed through SMART, and will be ignored.");
        errorDescriptions.put(132, "This order cannot be transmitted without a clearing account.");
        errorDescriptions.put(133, "Submit new order failed.");
        errorDescriptions.put(134, "Modify order failed.");
        errorDescriptions.put(136, "This order cannot be cancelled.");
        errorDescriptions.put(137, "VWAP orders can only be cancelled up to three minutes before the start time.");
        errorDescriptions.put(138, "Could not parse ticker request:");
        errorDescriptions.put(139, "Parsing error:");
        errorDescriptions.put(140, "The size value should be an integer:");
        errorDescriptions.put(141, "The price value should be a double:");
        errorDescriptions.put(142, "Institutional customer account does not have account info");
        errorDescriptions.put(143, "Requested ID is not an integer number.");
        errorDescriptions.put(144, "Order size does not match total share allocation.To adjust the share allocation, right-click on the order and select �Modify > Share Allocation�.");
        errorDescriptions.put(145, "Error in validating entry fields -");
        errorDescriptions.put(146, "Invalid trigger method.");
        errorDescriptions.put(147, "The conditional security info is incomplete.");
        errorDescriptions.put(148, "A conditional order can only be submitted when the order type is set to limit or market.");
        errorDescriptions.put(151, "This order cannot be transmitted without a user name.");
        errorDescriptions.put(152, "The 'hidden' order attribute may not be specified for this order.");
        errorDescriptions.put(153, "EFPs can only be limit orders.");
        errorDescriptions.put(154, "Orders cannot be transmitted for a halted security.");
        errorDescriptions.put(155, "A sizeOp order must have a username and account.");
        errorDescriptions.put(156, "A SizeOp order must go to IBSX");
        errorDescriptions.put(157, "An order can be EITHER Iceberg or Discretionary. Please remove either the Discretionary amount or the Display size.");
        errorDescriptions.put(158, "You must specify an offset amount or a percent offset value.");
        errorDescriptions.put(159, "The percent offset value must be between 0% and 100%.");
        errorDescriptions.put(160, "The size value cannot be zero.");
        errorDescriptions.put(161, "Cancel attempted when order is not in a cancellable state. Order permId =");
        errorDescriptions.put(162, "Historical market data Service error message.");
        errorDescriptions.put(163, "The price specified would violate the percentage constraint specified in the default order settings.");
        errorDescriptions.put(164, "There is no market data to check price percent violations.");
        errorDescriptions.put(165, "Historical market Data Service query message.");
        errorDescriptions.put(166, "HMDS Expired Security Violation.");
        errorDescriptions.put(167, "VWAP order time must be in the future.");
        errorDescriptions.put(168, "Discretionary amount does not conform to the minimum price variation for this security.");
        errorDescriptions.put(200, "No security definition has been found for the request.");
        errorDescriptions.put(201, "Order rejected - Reason:");
        errorDescriptions.put(203, "The security <security> is not available or allowed for this account.");
        errorDescriptions.put(300, "Can't find EId with ticker Id:");
        errorDescriptions.put(301, "Invalid ticker action:");
        errorDescriptions.put(302, "Error parsing stop ticker string:");
        errorDescriptions.put(303, "Invalid action:");
        errorDescriptions.put(304, "Invalid account value action:");
        errorDescriptions.put(305, "Request parsing error, the request has been ignored.");
        errorDescriptions.put(306, "Error processing DDE request:");
        errorDescriptions.put(307, "Invalid request topic:");
        errorDescriptions.put(308, "Unable to create the 'API' page in TWS as the maximum number of pages already exists.");
        errorDescriptions.put(309, "Max number (3) of market depth requests has been reached. TWS currently limits users to a maximum of 3 distinct market depth requests. This same restriction applies to API clients, however API clients may make multiple market depth requests for the same security.");
        errorDescriptions.put(310, "Can't find the subscribed market depth with tickerId:");
        errorDescriptions.put(311, "The origin is invalid.");
        errorDescriptions.put(312, "The combo details are invalid.");
        errorDescriptions.put(313, "The combo details for leg '<leg number>' are invalid.");
        errorDescriptions.put(314, "Security type 'BAG' requires combo leg details.");
        errorDescriptions.put(315, "Stock combo legs are restricted to SMART order routing.");
        errorDescriptions.put(316, "Market depth data has been HALTED. Please re-subscribe.");
        errorDescriptions.put(317, "Market depth data has been RESET. Please empty deep book contents before applying any new entries.");
        errorDescriptions.put(319, "Invalid log level <log level>");
        errorDescriptions.put(320, "Server error when reading an API client request.");
        errorDescriptions.put(321, "Server error when validating an API client request.");
        errorDescriptions.put(322, "Server error when processing an API client request.");
        errorDescriptions.put(323, "Server error: cause - %s");
        errorDescriptions.put(324, "Server error when reading a DDE client request (missing information).");
        errorDescriptions.put(325, "Discretionary orders are not supported for this combination of exchange and order type.");
        errorDescriptions.put(326, "Unable to connect as the client id is already in use. Retry with a unique client id.");
        errorDescriptions.put(327, "Only API connections with clientId set to 0 can set the auto bind TWS orders property.");
        errorDescriptions.put(328, "Trailing stop orders can be attached to limit or stop-limit orders only.");
        errorDescriptions.put(329, "Order modify failed. Cannot change to the new order type.");
        errorDescriptions.put(330, "Only FA or STL customers can request managed accounts list.");
        errorDescriptions.put(331, "Internal error. FA or STL does not have any managed accounts.");
        errorDescriptions.put(332, "The account codes for the order profile are invalid.");
        errorDescriptions.put(333, "Invalid share allocation syntax.");
        errorDescriptions.put(334, "Invalid Good Till Date order");
        errorDescriptions.put(335, "Invalid delta: The delta must be between 0 and 100.");
        errorDescriptions.put(336, "The time or time zone is invalid. The correct format is hh:mm:ss xxx where xxx is an optionally specified time-zone. E.g.: 15:59:00 EST Note that there is a space between the time and the time zone. If no time zone is specified, local time is assumed.");
        errorDescriptions.put(337, "The date, time, or time-zone entered is invalid. The correct format is yyyymmdd hh:mm:ss xxx where yyyymmdd and xxx are optional. E.g.: 20031126 15:59:00 EST Note that there is a space between the date and time, and between the time and time-zone., no date is specified, current date is assumed. If no time-zone is specified, local time-zone is assumed.");
        errorDescriptions.put(338, "Good After Time orders are currently disabled on this exchange.");
        errorDescriptions.put(339, "Futures spread are no longer supported. Please use combos instead.");
        errorDescriptions.put(340, "Invalid improvement amount for box auction strategy.");
        errorDescriptions.put(341, "Invalid delta. Valid values are from 1 to 100. You can set the delta from the 'Pegged to Stock' section of the Order Ticket Panel, or by selecting Page/Layout from the main menu and adding the Delta column.");
        errorDescriptions.put(342, "Pegged order is not supported on this exchange.");
        errorDescriptions.put(343, "The date, time, or time-zone entered is invalid. The correct format is yyyymmdd hh:mm:ss xxx where yyyymmdd and xxx are optional. E.g.: 20031126 15:59:00 EST Note that there is a space between the date and time, and between the time and time-zone.If no date is specified, current date is assumed. If no time-zone is specified, local time-zone is assumed.");
        errorDescriptions.put(344, "The account logged into is not a financial advisor account.");
        errorDescriptions.put(345, "Generic combo is not supported for FA advisor account.");
        errorDescriptions.put(346, "Not an institutional account or an away clearing account.");
        errorDescriptions.put(347, "Short sale slot value must be 1 (broker holds shares) or 2 (delivered from elsewhere).");
        errorDescriptions.put(348, "Order not a short sale -- type must be SSHORT to specify short sale slot.");
        errorDescriptions.put(349, "Generic combo does not support 'Good After' attribute.");
        errorDescriptions.put(350, "Minimum quantity is not supported for best combo order.");
        errorDescriptions.put(351, "The 'Regular Trading Hours only' flag is not valid for this order.");
        errorDescriptions.put(352, "Short sale slot value of 2 (delivered from elsewhere) requires location.");
        errorDescriptions.put(353, "Short sale slot value of 1 requires no location be specified.");
        errorDescriptions.put(354, "Not subscribed to requested market data.");
        errorDescriptions.put(355, "Order size does not conform to market rule.");
        errorDescriptions.put(356, "Smart-combo order does not support OCA group.");
        errorDescriptions.put(357, "Your client version is out of date.");
        errorDescriptions.put(358, "Smart combo child order not supported.");
        errorDescriptions.put(359, "Combo order only supports reduce on fill without block(OCA).");
        errorDescriptions.put(360, "No whatif check support for smart combo order.");
        errorDescriptions.put(361, "Invalid trigger price.");
        errorDescriptions.put(362, "Invalid adjusted stop price.");
        errorDescriptions.put(363, "Invalid adjusted stop limit price.");
        errorDescriptions.put(364, "Invalid adjusted trailing amount.");
        errorDescriptions.put(365, "No scanner subscription found for ticker id:");
        errorDescriptions.put(366, "No historical data query found for ticker id:");
        errorDescriptions.put(367, "Volatility type if set must be 1 or 2 for VOL orders. Do not set it for other order types.");
        errorDescriptions.put(368, "Reference Price Type must be 1 or 2 for dynamic volatility management. Do not set it for non-VOL orders.");
        errorDescriptions.put(369, "Volatility orders are only valid for US options.");
        errorDescriptions.put(370, "Dynamic Volatility orders must be SMART routed, or trade on a Price Improvement Exchange.");
        errorDescriptions.put(371, "VOL order requires positive floating point value for volatility. Do not set it for other order types.");
        errorDescriptions.put(372, "Cannot set dynamic VOL attribute on non-VOL order.");
        errorDescriptions.put(373, "Can only set stock range attribute on VOL or RELATIVE TO STOCK order.");
        errorDescriptions.put(374, "If both are set, the lower stock range attribute must be less than the upper stock range attribute.");
        errorDescriptions.put(375, "Stock range attributes cannot be negative.");
        errorDescriptions.put(376, "The order is not eligible for continuous update. The option must trade on a cheap-to-reroute exchange.");
        errorDescriptions.put(377, "Must specify valid delta hedge order aux. price.");
        errorDescriptions.put(378, "Delta hedge order type requires delta hedge aux. price to be specified.");
        errorDescriptions.put(379, "Delta hedge order type requires that no delta hedge aux. price be specified.");
        errorDescriptions.put(380, "This order type is not allowed for delta hedge orders.");
        errorDescriptions.put(381, "Your DDE.dll needs to be upgraded.");
        errorDescriptions.put(382, "The price specified violates the number of ticks constraint specified in the default order settings.");
        errorDescriptions.put(383, "The size specified violates the size constraint specified in the default order settings.");
        errorDescriptions.put(384, "Invalid DDE array request.");
        errorDescriptions.put(385, "Duplicate ticker ID for API scanner subscription.");
        errorDescriptions.put(386, "Duplicate ticker ID for API historical data query.");
        errorDescriptions.put(387, "Unsupported order type for this exchange and security type.");
        errorDescriptions.put(388, "Order size is smaller than the minimum requirement.");
        errorDescriptions.put(389, "Supplied routed order ID is not unique.");
        errorDescriptions.put(390, "Supplied routed order ID is invalid.");
        errorDescriptions.put(391, "The time or time-zone entered is invalid. The correct format is hh:mm:ss xxx where xxx is an optionally specified time-zone. E.g.: 15:59:00 EST. Note that there is a space between the time and the time zone.If no time zone is specified, local time is assumed.");
        errorDescriptions.put(392, "Invalid order: security expired.");
        errorDescriptions.put(393, "Short sale slot may be specified for delta hedge orders only.");
        errorDescriptions.put(394, "Invalid Process Time: must be integer number of milliseconds between 100 and 2000.  Found:");
        errorDescriptions.put(395, "Due to system problems, orders with OCA groups are currently not being accepted.");
        errorDescriptions.put(396, "Due to system problems, application is currently accepting only Market and Limit orders for this security.");
        errorDescriptions.put(397, "Due to system problems, application is currently accepting only Market and Limit orders for this security.");
        errorDescriptions.put(398, "< > cannot be used as a condition trigger.");
        errorDescriptions.put(400, "Algo order error.");
        errorDescriptions.put(401, "Length restriction.");
        errorDescriptions.put(402, "Conditions are not allowed for this security.");
        errorDescriptions.put(403, "Invalid stop price.");
        errorDescriptions.put(404, "Shares for this order are not immediately available for short sale. The order will be held while we attempt to locate the shares.");
        errorDescriptions.put(405, "The child order quantity should be equivalent to the parent order size.");
        errorDescriptions.put(406, "The currency < > is not allowed.");
        errorDescriptions.put(407, "The symbol should contain valid non-unicode characters only.");
        errorDescriptions.put(408, "Invalid scale order increment.");
        errorDescriptions.put(409, "Invalid scale order. You must specify order component size.");
        errorDescriptions.put(410, "Invalid subsequent component size for scale order.");
        errorDescriptions.put(411, "The 'Outside Regular Trading Hours' flag is not valid for this order.");
        errorDescriptions.put(412, "The security is not available for trading.");
        errorDescriptions.put(413, "What-if order should have the transmit flag set to true.");
        errorDescriptions.put(414, "Snapshot market data subscription is not applicable to generic ticks.");
        errorDescriptions.put(415, "Wait until previous RFQ finishes and try again.");
        errorDescriptions.put(416, "RFQ is not applicable for the security. Order ID:");
        errorDescriptions.put(417, "Invalid initial component size for scale order.");
        errorDescriptions.put(418, "Invalid scale order profit offset.");
        errorDescriptions.put(419, "Missing initial component size for scale order.");
        errorDescriptions.put(420, "Invalid real-time query.");
        errorDescriptions.put(421, "Invalid route.");
        errorDescriptions.put(422, "The account and clearing attributes on this order may not be changed.");
        errorDescriptions.put(423, "Cross order RFQ has been expired. THI committed size is no longer available. Please open order dialog and verify liquidity allocation.");
        errorDescriptions.put(424, "FA Order requires allocation to be specified.");
        errorDescriptions.put(425, "FA Order requires per-account manual allocations because there is no common clearing instruction. Please use order dialog Adviser tab to enter the allocation.");
        errorDescriptions.put(426, "None of the accounts have enough shares.");
        errorDescriptions.put(427, "Mutual Fund order requires monetary value to be specified.");
        errorDescriptions.put(428, "Mutual Fund Sell order requires shares to be specified.");
        errorDescriptions.put(429, "Delta neutral orders are only supported for combos (BAG security type).");
        errorDescriptions.put(430, "We are sorry, but fundamentals data for the security specified is not available.");
        errorDescriptions.put(431, "What to show field is missing or incorrect.");
        errorDescriptions.put(432, "Commission must not be negative.");
        errorDescriptions.put(433, "Invalid 'Restore size after taking profit' for multiple account allocation scale order.");
        errorDescriptions.put(434, "The order size cannot be zero.");
        errorDescriptions.put(435, "You must specify an account.");
        errorDescriptions.put(436, "You must specify an allocation (either a single account, group, or profile).");
        errorDescriptions.put(437, "Order can have only one flag Outside RTH or Allow PreOpen.");
        errorDescriptions.put(438, "The application is now locked.");
        errorDescriptions.put(439, "Order processing failed. Algorithm definition not found.");
        errorDescriptions.put(440, "Order modify failed. Algorithm cannot be modified.");
        errorDescriptions.put(441, "Algo attributes validation failed:");
        errorDescriptions.put(442, "Specified algorithm is not allowed for this order.");
        errorDescriptions.put(443, "Order processing failed. Unknown algo attribute.");
        errorDescriptions.put(444, "Volatility Combo order is not yet acknowledged. Cannot submit changes at this time.");
        errorDescriptions.put(445, "The RFQ for this order is no longer valid.");
        errorDescriptions.put(446, "Missing scale order profit offset.");
        errorDescriptions.put(447, "Missing scale price adjustment amount or interval.");
        errorDescriptions.put(448, "Invalid scale price adjustment interval.");
        errorDescriptions.put(449, "Unexpected scale price adjustment amount or interval.");
        errorDescriptions.put(40, "Dividend schedule query failed.");
        errorDescriptions.put(501, "Already connected.");
        errorDescriptions.put(503, "Your version of TWS is out of date and must be upgraded.");
        errorDescriptions.put(505, "Fatal error: Unknown message id.");
        errorDescriptions.put(510, "Request market data - sending error:");
        errorDescriptions.put(511, "Cancel market data - sending error:");
        errorDescriptions.put(512, "Order - sending error:");
        errorDescriptions.put(513, "Account update request - sending error:");
        errorDescriptions.put(514, "Request for executions  - sending error:");
        errorDescriptions.put(515, "Cancel order - sending error:");
        errorDescriptions.put(516, "Request open order - sending error:");
        errorDescriptions.put(517, "Unknown security. Verify the security details supplied.");
        errorDescriptions.put(518, "Request security data - sending error:");
        errorDescriptions.put(519, "Request market depth - sending error:");
        errorDescriptions.put(520, "Cancel market depth - sending error:");
        errorDescriptions.put(521, "Set server log level - sending error:");
        errorDescriptions.put(522, "FA Information Request - sending error:");
        errorDescriptions.put(523, "FA Information Replace - sending error:");
        errorDescriptions.put(524, "Request Scanner subscription - sending error:");
        errorDescriptions.put(525, "Cancel Scanner subscription - sending error:");
        errorDescriptions.put(526, "Request Scanner parameter - sending error:");
        errorDescriptions.put(527, "Request Historical data - sending error:");
        errorDescriptions.put(528, "Cancel Historical data - sending error:");
        errorDescriptions.put(529, "Request real-time bar data - sending error:");
        errorDescriptions.put(530, "Cancel real-time bar data - sending error:");
        errorDescriptions.put(531, "Request Current Time - Sending error: ");
        errorDescriptions.put(2100, "New account data requested from TWS.  API client has been unsubscribed from account data.");
        errorDescriptions.put(2101, "Unable to subscribe to account as the following clients are subscribed to a different account.");
        errorDescriptions.put(2102, "Unable to modify this order as it is still being processed.");
        errorDescriptions.put(2103, "A market data farm is disconnected.");
        errorDescriptions.put(2104, "A market data farm is connected.");
        errorDescriptions.put(2105, "A historical data farm is disconnected.");
        errorDescriptions.put(2106, "A historical data farm is connected.");
        errorDescriptions.put(2107, "A historical data farm connection has become inactive but should be available upon demand.");
        errorDescriptions.put(2108, "A market data farm connection has become inactive but should be available upon demand.");
        errorDescriptions.put(2109, "Order Event Warning: Attribute �Outside Regular Trading Hours� is ignored based on the order type and destination. PlaceOrder is now processed.");
    }

    private ErrorCodes() {
    }

    public static String getDescription(int code) {
        return errorDescriptions.get(code);
    }

    public static ErrorSection getSection(int code) {
        final ErrorSection section = errorSections.get(code);
        return section == null ? ErrorSection.UNKNOWN : section;
    }

    private static void initError(int code, ErrorSection section, String description) {
        errorSections.put(code, section);
        errorDescriptions.put(code, description);
    }
}
