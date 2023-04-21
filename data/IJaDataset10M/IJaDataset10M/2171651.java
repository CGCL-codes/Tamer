package com.handcoded.fpml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.handcoded.classification.AbstractCategory;
import com.handcoded.classification.Category;
import com.handcoded.classification.RefinableCategory;
import com.handcoded.xml.DOM;
import com.handcoded.xml.XPath;

/**
 * The <CODE>ProductType</CODE> class contains a set of <CODE>Category</CODE>
 * instances configured to classify standard FpML product types.
 * <P>
 * In addition to specific product types a number of 'abstract' categories
 * are defined such as <CODE>(OPTION)</CODE> and <CODE>(INTEREST RATE DERIVATIVE)
 * </CODE> to allow high level partitioning.
 * 
 * @author 	BitWise
 * @version	$Id: ProductType.java 436 2010-04-17 19:23:36Z andrew_jacobs $
 * @since	TFP 1.0
 */
public final class ProductType {

    /**
	 * A <CODE>Category</CODE> representing all short form documents.
	 * @since	TFP 1.0
	 */
    public static final Category SHORT_FORM = new AbstractCategory("(SHORT FORM)");

    /**
	 * A <CODE>Category</CODE> representing all product types.
	 * @since	TFP 1.0
	 */
    public static final Category PRODUCT_TYPE = new AbstractCategory("(PRODUCT TYPE)");

    /**
	 * A <CODE>Category</CODE> representing all structured products.
	 * @since	TFP 1.0
	 */
    public static final Category STRUCTURED_PRODUCT = new RefinableCategory("(STRUCTURED PRODUCT)", PRODUCT_TYPE) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            Document document = ((Element) value).getOwnerDocument();
            if (Releases.FPML.getReleaseForDocument(document) == Releases.R1_0) return (XPath.path((Element) value, "product", "strategy") != null); else return (XPath.path((Element) value, "strategy") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all swaps.
	 * @since	TFP 1.0
	 */
    public static final Category SWAP = new AbstractCategory("(SWAP)", PRODUCT_TYPE);

    /**
	 * A <CODE>Category</CODE> representing all options.
	 * @since	TFP 1.0
	 */
    public static final Category OPTION = new AbstractCategory("(OPTION)", PRODUCT_TYPE);

    /***
	 * A <CODE>Category</CODE> representing all forwards.
	 * @since	TFP 1.0
	 */
    public static final Category FORWARD = new AbstractCategory("(FORWARD)", PRODUCT_TYPE);

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange products.
	 * @since  	TFP 1.0
	 */
    public static final Category FOREIGN_EXCHANGE = new AbstractCategory("(FOREIGN EXCHANGE)", PRODUCT_TYPE);

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange spots/forwards.
	 * @since  	TFP 1.0
	 */
    public static final Category FX_SPOT_FORWARD = new RefinableCategory("FX SPOT/FORWARD", FOREIGN_EXCHANGE) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "fxSingleLeg") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange spots.
	 * @since  	TFP 1.0
	 */
    public static final Category FX_SPOT = new RefinableCategory("FX SPOT", FX_SPOT_FORWARD) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "fxSingleLeg", "exchangeRate", "forwardPoints") == null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange forwards.
	 * @since  	TFP 1.0
	 */
    public static final Category FX_FORWARD = new RefinableCategory("FX FORWARD", new Category[] { FX_SPOT_FORWARD, FORWARD }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "fxSingleLeg", "exchangeRate", "forwardPoints") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange swaps.
	 * @since  	TFP 1.0
	 */
    public static final Category FX_SWAP = new RefinableCategory("FX SWAP", new Category[] { FOREIGN_EXCHANGE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "fxSwap") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange options.
	 * @since  	TFP 1.0
	 */
    public static final Category FX_OPTION = new AbstractCategory("FX OPTION", new Category[] { FOREIGN_EXCHANGE, OPTION });

    /**
	 * A <CODE>Category</CODE> representing all simple foreign exchange options.
	 * @since  	TFP 1.0
	 */
    public static final Category FX_SIMPLE_OPTION = new RefinableCategory("FX SIMPLE OPTION", FX_OPTION) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "fxSimpleOption") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange barrier options.
	 * @since  	TFP 1.0
	 */
    public static final Category FX_BARRIER_OPTION = new RefinableCategory("FX BARRIER OPTION", FX_OPTION) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "fxBarrierOption") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange digital options.
	 * @since  	TFP 1.1
	 */
    public static final Category FX_DIGITAL_OPTION = new RefinableCategory("FX DIGITAL OPTION", FX_OPTION) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "fxDigitalOption") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange average rate
	 * options.
	 * @since  	TFP 1.2
	 */
    public static final Category FX_AVERATE_RATE_OPTION = new RefinableCategory("FX AVERAGE RATE OPTION", FX_OPTION) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "fxAverageRateOption") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all foreign exchange option strategies.
	 * @since  	TFP 1.2
	 */
    public static final Category FX_OPTION_STRATEGY = new RefinableCategory("FX OPTION STRATEGY", new Category[] { FOREIGN_EXCHANGE, STRUCTURED_PRODUCT }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            NodeList nodes = XPath.paths((Element) value, "strategy", "*");
            if (nodes.getLength() > 0) {
                for (int index = 0; index < nodes.getLength(); ++index) {
                    String localName = ((Element) nodes.item(index)).getLocalName();
                    if (localName.equals("productType")) continue;
                    if (localName.equals("productId")) continue;
                    if (localName.equals("fxSingleLeg")) continue;
                    if (localName.equals("fxSimpleOption")) continue;
                    if (localName.equals("fxBarrierOption")) continue;
                    if (localName.equals("fxDigitalOption")) continue;
                    if (localName.equals("fxAverageRateOption")) continue;
                    if (localName.equals("termDeposit")) continue;
                    return (false);
                }
                return (true);
            }
            return (false);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all bullet payments.
	 * @since 	TFP 1.0
	 */
    public static final Category BULLET_PAYMENT = new RefinableCategory("BULLET PAYMENT", PRODUCT_TYPE) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "bulletPayment") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all interest rate derivatives.
	 * @since	TFP 1.0
	 */
    public static final Category INTEREST_RATE_DERIVATIVE = new AbstractCategory("(INTEREST RATE DERIVATIVE)", PRODUCT_TYPE);

    /**
	 * A <CODE>Category</CODE> representing all forward rate agreements.
	 * @since	TFP 1.0
	 */
    public static final Category FORWARD_RATE_AGREEMENT = new RefinableCategory("FORWARD RATE AGREEMENT", INTEREST_RATE_DERIVATIVE) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            Document document = ((Element) value).getOwnerDocument();
            if (Releases.FPML.getReleaseForDocument(document) == Releases.R1_0) return (XPath.path((Element) value, "product", "fra") != null); else return (XPath.path((Element) value, "fra") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all interest rate swaps.
	 * @since 	TFP 1.0
	 */
    public static final Category INTEREST_RATE_SWAP = new RefinableCategory("INTEREST RATE SWAP", new Category[] { INTEREST_RATE_DERIVATIVE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            Document document = ((Element) value).getOwnerDocument();
            if (Releases.FPML.getReleaseForDocument(document) == Releases.R1_0) return (XPath.path((Element) value, "product", "swap") != null); else return (XPath.path((Element) value, "swap") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all cross currency interest rate swaps.
	 * @since 	TFP 1.0
	 */
    public static final Category CROSS_CURRENCY_SWAP = new RefinableCategory("CROSS CURRENCY SWAP", INTEREST_RATE_SWAP) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            Document document = ((Element) value).getOwnerDocument();
            NodeList currencies;
            boolean different = false;
            if (Releases.FPML.getReleaseForDocument(document) == Releases.R1_0) currencies = XPath.paths((Element) value, "product", "swap", "swapStream", "calculationPeriodAmount", "calculation", "notionalSchedule", "notionalStepSchedule", "currency"); else currencies = XPath.paths((Element) value, "swap", "swapStream", "calculationPeriodAmount", "calculation", "notionalSchedule", "notionalStepSchedule", "currency");
            for (int index = 1; index < currencies.getLength(); ++index) {
                Element ccy1 = (Element) currencies.item(index - 1);
                Element ccy2 = (Element) currencies.item(index);
                if (!DOM.getInnerText(ccy1).equals(DOM.getInnerText(ccy2))) {
                    different = true;
                    break;
                }
            }
            return (different);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all inflation swaps.
	 * @since	TFP 1.0
	 */
    public static final Category INFLATION_SWAP = new RefinableCategory("INFLATION SWAP", INTEREST_RATE_SWAP) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            NodeList calcs = XPath.paths((Element) value, "swap", "swapStream", "calculationPeriodAmount", "calculation", "inflationRateCalculation");
            return (calcs.getLength() > 0);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all term deposits.
	 * @since	TFP 1.0
	 */
    public static final Category TERM_DEPOSIT = new RefinableCategory("TERM DEPOSIT", PRODUCT_TYPE) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "termDeposit") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all interest rate caps and floors.
	 * @since 	TFP 1.0
	 */
    public static final Category INTEREST_RATE_CAP_FLOOR = new RefinableCategory("INTEREST RATE CAP/FLOOR", INTEREST_RATE_DERIVATIVE) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "capFloor") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all interest rate caps.
	 * @since 	TFP 1.0
	 */
    public static final Category INTEREST_RATE_CAP = new RefinableCategory("INTEREST RATE CAP", INTEREST_RATE_CAP_FLOOR) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            Element floatingRateCalculation = XPath.path((Element) value, "capFloor", "capFloorStream", "calculationPeriodAmount", "calculation", "floatingRateCalculation");
            Element capRateSchedule = XPath.path(floatingRateCalculation, "capRateSchedule");
            Element floorRateSchedule = XPath.path(floatingRateCalculation, "floorRateSchedule");
            return ((capRateSchedule != null) && (floorRateSchedule == null));
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all interest rate floors.
	 * @since 	TFP 1.0
	 */
    public static final Category INTEREST_RATE_FLOOR = new RefinableCategory("INTEREST RATE FLOOR", INTEREST_RATE_CAP_FLOOR) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            Element floatingRateCalculation = XPath.path((Element) value, "capFloor", "capFloorStream", "calculationPeriodAmount", "calculation", "floatingRateCalculation");
            Element capRateSchedule = XPath.path(floatingRateCalculation, "capRateSchedule");
            Element floorRateSchedule = XPath.path(floatingRateCalculation, "floorRateSchedule");
            return ((capRateSchedule == null) && (floorRateSchedule != null));
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all interest rate collars.
	 * @since 	TFP 1.0
	 */
    public static final Category INTEREST_RATE_COLLAR = new RefinableCategory("INTEREST RATE COLLAR", new Category[] { INTEREST_RATE_CAP, INTEREST_RATE_FLOOR }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            Element floatingRateCalculation = XPath.path((Element) value, "capFloor", "capFloorStream", "calculationPeriodAmount", "calculation", "floatingRateCalculation");
            Element capRateSchedule = XPath.path(floatingRateCalculation, "capRateSchedule");
            Element floorRateSchedule = XPath.path(floatingRateCalculation, "floorRateSchedule");
            return ((capRateSchedule != null) && (floorRateSchedule != null));
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all interest rate swaptions.
	 * @since 	TFP 1.0
	 */
    public static final Category INTEREST_RATE_SWAPTION = new RefinableCategory("INTEREST RATE SWAPTION", new Category[] { INTEREST_RATE_DERIVATIVE, OPTION }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "swaption") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity derivatives.
	 * @since	TFP 1.0
	 */
    public static final Category EQUITY_DERIVATIVE = new AbstractCategory("(EQUITY DERIVATIVE)", PRODUCT_TYPE);

    /**
	 * A <CODE>Category</CODE> representing all equity forwards.
	 * @since	TFP 1.0
	 */
    public static final Category EQUITY_FORWARD = new RefinableCategory("EQUITY FORWARD", new Category[] { EQUITY_DERIVATIVE, FORWARD }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "equityForward") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity derivatives.
	 * @since	TFP 1.0
	 */
    public static final Category EQUITY_OPTION = new AbstractCategory("EQUITY OPTION", PRODUCT_TYPE);

    /**
	 * A <CODE>Category</CODE> representing all simple equity options.
	 * @since	TFP 1.0
	 */
    public static final Category EQUITY_SIMPLE_OPTION = new RefinableCategory("EQUITY SIMPLE OPTION", EQUITY_OPTION) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "equityOption") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity short form options.
	 * @since	TFP 1.0
	 */
    public static final Category EQUITY_OPTION_SHORT_FORM = new RefinableCategory("EQUITY OPTION SHORT FORM", new Category[] { EQUITY_OPTION, SHORT_FORM }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "brokerEquityOption") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity option tranaction supplements.
	 * @since	TFP 1.0
	 */
    public static final Category EQUITY_OPTION_TRANSACTION_SUPPLEMENT = new RefinableCategory("EQUITY OPTION TRANSACTION SUPPLEMENT", EQUITY_OPTION) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "equityOptionTransactionSupplement") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity correlation swaps.
	 * @since	TFP 1.2
	 */
    public static final Category EQUITY_CORRELATION_SWAP = new RefinableCategory("EQUITY CORRELATION SWAP", new Category[] { EQUITY_DERIVATIVE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "correlationSwap") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity dividend swaps.
	 * @since	TFP 1.2
	 */
    public static final Category EQUITY_DIVIDEND_SWAP = new RefinableCategory("EQUITY DIVIDEND SWAP", new Category[] { EQUITY_DERIVATIVE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "dividendSwapTransactionSupplement") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity variance options.
	 * @since	TFP 1.4
	 */
    public static final Category EQUITY_VARIANCE_OPTION = new RefinableCategory("EQUITY VARIANCE OPTION", EQUITY_OPTION) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "varianceOptionTransactionSupplement") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity variance swaps.
	 * @since	TFP 1.2
	 */
    public static final Category EQUITY_VARIANCE_SWAP = new RefinableCategory("EQUITY VARIANCE SWAP", new Category[] { EQUITY_DERIVATIVE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "varianceSwap") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity variance swap transaction supplements.
	 * @since	TFP 1.4
	 */
    public static final Category EQUITY_VARIANCE_SWAP_TRANSACTION_SUPPLEMENT = new RefinableCategory("EQUITY VARIANCE SWAP TRANSACTION SUPPLEMENT", new Category[] { EQUITY_DERIVATIVE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "varianceSwapTransactionSupplement") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity total return swaps.
	 * @since	TFP 1.2
	 */
    public static final Category EQUITY_TOTAL_RETURN_SWAP = new RefinableCategory("EQUITY TOTAL RETURN SWAP", new Category[] { EQUITY_DERIVATIVE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "returnSwap") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all equity swap transaction supplements.
	 * @since	TFP 1.2
	 */
    public static final Category EQUITY_SWAP_TRANSACTION_SUPPLEMENT = new RefinableCategory("EQUITY SWAP TRANSACTION SUPPLEMENT", new Category[] { EQUITY_DERIVATIVE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "equitySwapTransactionSupplement") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all fixed income products.
	 * @since	TFP 1.4
	 */
    public static final Category FIXED_INCOME = new AbstractCategory("(FIXED INCOME)", PRODUCT_TYPE);

    /**
	 * A <CODE>Category</CODE> representing all bond options.
	 * @since	TFP 1.2
	 */
    public static final Category BOND_OPTION = new RefinableCategory("BOND OPTION", new Category[] { FIXED_INCOME, OPTION }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "bondOption") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all credit derivatives.
	 * @since	TFP 1.0
	 */
    public static final Category CREDIT_DERIVATIVE = new AbstractCategory("(CREDIT DERIVATIVE)", PRODUCT_TYPE);

    /**
	 * A <CODE>Category</CODE> representing all credit default swaps.
	 * @since	TFP 1.0
	 */
    public static final Category CREDIT_DEFAULT_SWAP = new RefinableCategory("CREDIT DEFAULT SWAP", new Category[] { CREDIT_DERIVATIVE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "creditDefaultSwap") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all credit default swaptions.
	 * @since	TFP 1.2
	 */
    public static final Category CREDIT_DEFAULT_SWAPTION = new RefinableCategory("CREDIT DEFAULT SWAPTION", new Category[] { CREDIT_DERIVATIVE, OPTION }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "creditDefaultSwapOption") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all commodity derivatives.
	 * @since	TFP 1.4
	 */
    public static final Category COMMODITY_DERIVATIVE = new AbstractCategory("(COMMODITY DERIVATIVE)", PRODUCT_TYPE);

    /**
	 * A <CODE>Category</CODE> representing all commodity swaps.
	 * @since	TFP 1.4
	 */
    public static final Category COMMODITY_SWAP = new RefinableCategory("COMMODITY SWAP", new Category[] { COMMODITY_DERIVATIVE, SWAP }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "commoditySwap") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all commodity forwards.
	 * @since	TFP 1.4
	 */
    public static final Category COMMODITY_FORWARD = new RefinableCategory("COMMODITY FORWARD", new Category[] { COMMODITY_DERIVATIVE, FORWARD }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "commodityForward") != null);
        }
    };

    /**
	 * A <CODE>Category</CODE> representing all commodity options.
	 * @since	TFP 1.4
	 */
    public static final Category COMMODITY_OPTION = new RefinableCategory("COMMODITY OPTION", new Category[] { COMMODITY_DERIVATIVE, OPTION }) {

        /**
			 * {@inheritDoc}
			 */
        protected boolean isApplicable(final Object value) {
            return (XPath.path((Element) value, "commodityOption") != null);
        }
    };

    /**
	 * Attempts to determine the type of product used within a trade.
	 *
	 * @param	trade			A trade <CODE>Element</CODE>.
	 * @return	A <CODE>Category</CODE> instance based on the trades
	 * 			contained product type, or <CODE>null</CODE>.
	 * @since	TFP 1.0
	 */
    public static Category classify(Element trade) {
        return (PRODUCT_TYPE.classify(trade));
    }

    /**
	 * Ensures that no instance can be constructed.
	 * @since	TFP 1.0
	 */
    private ProductType() {
    }
}
