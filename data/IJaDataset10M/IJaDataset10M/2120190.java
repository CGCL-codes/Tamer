package com.globant.google.mendoza.malbec;

/** The buyer discount coupon that will be entered in the place order page.
*/
public class BuyerCoupon implements BuyerInformation {

    /** The coupon code to send.
   */
    private String couponCode;

    /** Creates the data that the buyer posts when adding coupons before placing
   *  an order.
   */
    public BuyerCoupon(final String theCouponCode) {
        if (theCouponCode == null) {
            throw new IllegalArgumentException("the coupon code cannot be null");
        }
        couponCode = theCouponCode;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void execute(BuyerVisitor visitor) {
        visitor.enterCoupon(this);
    }
}
