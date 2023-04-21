package si.fri.pis.netStore;

/**
     *  B2BWs2CallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
public abstract class B2BWs2CallbackHandler {

    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public B2BWs2CallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public B2BWs2CallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
            * auto generated Axis2 call back method for resetCart method
            * override this method for handling normal response from resetCart operation
            */
    public void receiveResultresetCart(si.fri.pis.netStore.B2BWs2Stub.ResetCartResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from resetCart operation
           */
    public void receiveErrorresetCart(java.lang.Exception e) {
    }

    /**
            * auto generated Axis2 call back method for buyNow method
            * override this method for handling normal response from buyNow operation
            */
    public void receiveResultbuyNow(si.fri.pis.netStore.B2BWs2Stub.BuyNowResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buyNow operation
           */
    public void receiveErrorbuyNow(java.lang.Exception e) {
    }

    /**
            * auto generated Axis2 call back method for removeProductFromCart method
            * override this method for handling normal response from removeProductFromCart operation
            */
    public void receiveResultremoveProductFromCart(si.fri.pis.netStore.B2BWs2Stub.RemoveProductFromCartResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from removeProductFromCart operation
           */
    public void receiveErrorremoveProductFromCart(java.lang.Exception e) {
    }

    /**
            * auto generated Axis2 call back method for buyItemInCart method
            * override this method for handling normal response from buyItemInCart operation
            */
    public void receiveResultbuyItemInCart(si.fri.pis.netStore.B2BWs2Stub.BuyItemInCartResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buyItemInCart operation
           */
    public void receiveErrorbuyItemInCart(java.lang.Exception e) {
    }

    /**
            * auto generated Axis2 call back method for getItem method
            * override this method for handling normal response from getItem operation
            */
    public void receiveResultgetItem(si.fri.pis.netStore.B2BWs2Stub.GetItemResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getItem operation
           */
    public void receiveErrorgetItem(java.lang.Exception e) {
    }

    /**
            * auto generated Axis2 call back method for buyItemsInCart method
            * override this method for handling normal response from buyItemsInCart operation
            */
    public void receiveResultbuyItemsInCart(si.fri.pis.netStore.B2BWs2Stub.BuyItemsInCartResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from buyItemsInCart operation
           */
    public void receiveErrorbuyItemsInCart(java.lang.Exception e) {
    }

    /**
            * auto generated Axis2 call back method for getItemsByPriceLowerThan method
            * override this method for handling normal response from getItemsByPriceLowerThan operation
            */
    public void receiveResultgetItemsByPriceLowerThan(si.fri.pis.netStore.B2BWs2Stub.GetItemsByPriceLowerThanResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getItemsByPriceLowerThan operation
           */
    public void receiveErrorgetItemsByPriceLowerThan(java.lang.Exception e) {
    }

    /**
            * auto generated Axis2 call back method for getProductsInCart method
            * override this method for handling normal response from getProductsInCart operation
            */
    public void receiveResultgetProductsInCart(si.fri.pis.netStore.B2BWs2Stub.GetProductsInCartResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getProductsInCart operation
           */
    public void receiveErrorgetProductsInCart(java.lang.Exception e) {
    }

    /**
            * auto generated Axis2 call back method for addProductToCart method
            * override this method for handling normal response from addProductToCart operation
            */
    public void receiveResultaddProductToCart(si.fri.pis.netStore.B2BWs2Stub.AddProductToCartResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addProductToCart operation
           */
    public void receiveErroraddProductToCart(java.lang.Exception e) {
    }

    /**
            * auto generated Axis2 call back method for getItems method
            * override this method for handling normal response from getItems operation
            */
    public void receiveResultgetItems(si.fri.pis.netStore.B2BWs2Stub.GetItemsResponse result) {
    }

    /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getItems operation
           */
    public void receiveErrorgetItems(java.lang.Exception e) {
    }
}
