package com.google.checkout.sample.protocol;

import com.google.checkout.sample.util.StringUtil;
import com.google.checkout.schema._2.CouponResult;
import com.google.checkout.schema._2.GiftCertificateResult;
import com.google.checkout.schema._2.MerchantCalculationResults;
import com.google.checkout.schema._2.Result;
import com.google.checkout.schema._2.MerchantCalculationResults.Results;
import com.google.checkout.schema._2.Result.MerchantCodeResults;
import org.w3c.dom.Document;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

/**
 * The <b>MerchantCalculationResultBuilder</b> class contains methods that 
 * create the JAXB objects needed to construct Merchant Calculations API 
 * responses.
 * @version 1.0 beta
 */
public class MerchantCalculationResultBuilder extends AbstractProtocolBuilder {

    private static MerchantCalculationResultBuilder _builder;

    private MerchantCalculationResultBuilder() throws ProtocolException, JAXBException {
    }

    /**
   * Default constructor 
   */
    public static MerchantCalculationResultBuilder getInstance() throws ProtocolException {
        try {
            if (_builder == null) {
                _builder = new MerchantCalculationResultBuilder();
            }
        } catch (JAXBException jaxbEx) {
            throw new ProtocolException(jaxbEx);
        }
        return _builder;
    }

    /**
   * The <b>createCouponResult</b> method constructs a 
   * &lt;coupon-calculation&gt; result. Also see: {@see CouponResult}
   * @param isCodeValid Optional. Boolean flag that is true if coupon code 
   * is valid.
   * @param couponValue Optional. The value of the coupon.
   * @param couponCode Required. The coupon code.
   * @param additionalMsg Optional. A message to send to the buyer that will 
   * display next to the coupon code.
   * @throws ProtocolException if coupon code is empty
   */
    public CouponResult createCouponResult(boolean isCodeValid, float couponValue, String couponCode, String additionalMsg) throws ProtocolException {
        if (StringUtil.isEmpty(couponCode)) {
            throw new ProtocolException("coupon code cannot be empty");
        }
        CouponResult cResult = _objectFact.createCouponResult();
        cResult.setCalculatedAmount(createMoney(couponValue));
        cResult.setCode(couponCode);
        cResult.setValid(isCodeValid);
        cResult.setMessage(additionalMsg);
        return cResult;
    }

    /**
   * The <b>createGiftCertResult</b> method constructs a 
   * &lt;gift-certificate-calculation&gt; result. Also see 
   * {@see GiftCertificateResult}
   * @param isCodeValid Optional. Boolean flag that is true if gift certificate
   * code is valid
   * @param certificateValue Optional. The value of the gift certificate.
   * @param certificateCode Required. The gift certificate code.
   * @param additionalMsg Optional. A message for the buyer that will 
   * display next to the gift certificate code.
   * @throws ProtocolException if gift certificate code is empty
   */
    public GiftCertificateResult createGiftCertResult(boolean isCodeValid, float certificateValue, String certificateCode, String additionalMsg) throws ProtocolException {
        if (StringUtil.isEmpty(certificateCode)) {
            throw new ProtocolException();
        }
        GiftCertificateResult gResult = _objectFact.createGiftCertificateResult();
        gResult.setCalculatedAmount(createMoney(certificateValue));
        gResult.setCode(certificateCode);
        gResult.setValid(isCodeValid);
        gResult.setMessage(additionalMsg);
        return gResult;
    }

    /**
   * The <b>createMerchantCalResultsByGiftCert</b> method creates a 
   * &lt;merchant-calculation-results&gt; response with a list of 
   * gift certificates but no coupons.
   * @param giftResults A list of GiftCertificateResult objects
   * @param totalTaxAmount The total tax amount to assess for the order
   * @param shippingRate The cost to ship the order.
   * @param isShippable A Boolean flag indicating whether the order can
   * be shipping to the specified address using the requested shipping method.
   * @param addressId An ID for the shipping address.
   */
    public Document createMerchantCalResultsByGiftCert(List<GiftCertificateResult> giftResults, float totalTaxAmount, float shippingRate, boolean isShippable, String addressId) throws ProtocolException {
        return createMerchantCalResults(null, giftResults, totalTaxAmount, shippingRate, isShippable, addressId);
    }

    /**
   * The <b>createMerchantCalResultsByCoupon</b> method creates a 
   * &lt;merchant-calculation-results&gt; response with a list of coupons
   * but no gift certificates.
   * @param couponResults A list of CouponResult objects
   * @param totalTaxAmount The total tax amount to assess for the order
   * @param shippingRate The cost to ship the order.
   * @param isShippable A Boolean flag indicating whether the order can
   * be shipping to the specified address using the requested shipping method.
   * @param addressId An ID for the shipping address.
   */
    public Document createMerchantCalResultsByCoupon(List<CouponResult> couponResults, float totalTaxAmount, float shippingRate, boolean isShippable, String addressId) throws ProtocolException {
        return createMerchantCalResults(couponResults, null, totalTaxAmount, shippingRate, isShippable, addressId);
    }

    /**
   * The <b>createMerchantCalResults</b> method creates a 
   * &lt;merchant-calculation-results&gt; response with a list of coupons
   * and a list of gift certificates. The <code>createMerchantCalResultsByCoupon</b>
   * and <code>createMerchantCalResultsByGiftCert</code> methods both call
   * this method to create a &lt;merchant-calculation-results&gt; response.
   * @param couponList A list of CouponResult objects
   * @param giftCertList A list of GiftCertificateResult objects
   * @param totalTaxAmount The total tax amount to assess for the order
   * @param shippingRate The cost to ship the order.
   * @param isShippable A Boolean flag indicating whether the order can
   * be shipping to the specified address using the requested shipping method.
   * @param addressId An ID for the shipping address.
   */
    public Document createMerchantCalResults(List<CouponResult> couponList, List<GiftCertificateResult> giftCertList, float totalTaxAmount, float shippingRate, boolean isShippable, String addressId) throws ProtocolException {
        MerchantCodeResults merchantCodeResults = _objectFact.createResultMerchantCodeResults();
        List list = merchantCodeResults.getCouponResultOrGiftCertificateResult();
        if (couponList != null) {
            for (Object coupon : couponList) {
                list.add(coupon);
            }
        }
        if (giftCertList != null) {
            for (Object giftCert : giftCertList) {
                list.add(giftCert);
            }
        }
        Result individualResult = _objectFact.createResult();
        individualResult.setAddressId(addressId);
        individualResult.setShippable(isShippable);
        individualResult.setShippingRate(createMoney(shippingRate));
        individualResult.setTotalTax(createMoney(totalTaxAmount));
        individualResult.setMerchantCodeResults(merchantCodeResults);
        Results results = _objectFact.createMerchantCalculationResultsResults();
        results.getResult().add(individualResult);
        MerchantCalculationResults mResults = _objectFact.createMerchantCalculationResults();
        mResults.setResults(results);
        JAXBElement elem = _objectFact.createMerchantCalculationResults(mResults);
        return convertToDOM(elem);
    }

    /**
   * The <b>createMerchantCalResultsDOM</b> converts a 
   * MerchantCalculationResults DOM object to a 
   * &lt;merchant-calculation-results&gt; XML response
   * @deprecated
   */
    public Document createMerchantCalResultsDOM(MerchantCalculationResults results) throws ProtocolException {
        JAXBElement elem = _objectFact.createMerchantCalculationResults(results);
        return convertToDOM(elem);
    }
}
