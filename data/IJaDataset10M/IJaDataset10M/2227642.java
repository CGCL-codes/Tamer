package org.broadleafcommerce.order.web;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.catalog.service.CatalogService;
import org.broadleafcommerce.order.domain.FulfillmentGroup;
import org.broadleafcommerce.order.domain.FulfillmentGroupImpl;
import org.broadleafcommerce.order.domain.Order;
import org.broadleafcommerce.order.domain.OrderItem;
import org.broadleafcommerce.order.service.CartService;
import org.broadleafcommerce.order.service.FulfillmentGroupService;
import org.broadleafcommerce.order.service.exception.ItemNotFoundException;
import org.broadleafcommerce.order.web.model.AddToCartItem;
import org.broadleafcommerce.order.web.model.AddToCartItems;
import org.broadleafcommerce.order.web.model.CartOrderItem;
import org.broadleafcommerce.order.web.model.CartSummary;
import org.broadleafcommerce.pricing.service.exception.PricingException;
import org.broadleafcommerce.profile.domain.Customer;
import org.broadleafcommerce.profile.web.CustomerState;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller("blCartController")
@SessionAttributes("cartSummary")
public class CartController {

    private static final Log LOG = LogFactory.getLog(CartController.class);

    @Resource(name = "blCartService")
    protected final CartService cartService;

    @Resource(name = "blCustomerState")
    protected final CustomerState customerState;

    @Resource(name = "blCatalogService")
    protected final CatalogService catalogService;

    @Resource(name = "blFulfillmentGroupService")
    protected final FulfillmentGroupService fulfillmentGroupService;

    protected String cartView;

    protected boolean cartViewRedirect;

    protected String addItemView;

    protected boolean addItemViewRedirect;

    protected String removeItemView;

    protected boolean removeItemViewRedirect;

    public CartController() {
        this.cartService = null;
        this.customerState = null;
        this.catalogService = null;
        this.fulfillmentGroupService = null;
    }

    public void setCartView(String cartView) {
        this.cartView = cartView;
    }

    public void setAddItemView(String addItemView) {
        this.addItemView = addItemView;
    }

    public void setCartViewRedirect(boolean cartViewRedirect) {
        this.cartViewRedirect = cartViewRedirect;
    }

    public void setAddItemViewRedirect(boolean addItemViewRedirect) {
        this.addItemViewRedirect = addItemViewRedirect;
    }

    public void setRemoveItemView(String removeItemView) {
        this.removeItemView = removeItemView;
    }

    public void setRemoveItemViewRedirect(boolean removeItemViewRedirect) {
        this.removeItemViewRedirect = removeItemViewRedirect;
    }

    @RequestMapping(value = "addItem.htm", method = { RequestMethod.GET, RequestMethod.POST })
    public String addItem(@RequestParam(required = false) Boolean ajax, @ModelAttribute AddToCartItem addToCartItem, BindingResult errors, ModelMap model, HttpServletRequest request) {
        if (ajax == null) {
            ajax = false;
        }
        Order currentCartOrder = retrieveCartOrder(request, model);
        List<OrderItem> orderItemsAdded = new ArrayList<OrderItem>();
        if (addToCartItem.getQuantity() > 0) {
            try {
                OrderItem orderItem = cartService.addSkuToOrder(currentCartOrder.getId(), addToCartItem.getSkuId(), addToCartItem.getProductId(), addToCartItem.getCategoryId(), addToCartItem.getQuantity());
                orderItemsAdded.add(orderItem);
            } catch (PricingException e) {
                LOG.error("Unable to price the order: (" + currentCartOrder.getId() + ")", e);
            }
        }
        model.addAttribute("orderItemsAdded", orderItemsAdded);
        if (!ajax) {
            return addItemViewRedirect ? "redirect:" + addItemView : addItemView;
        } else {
            return "catalog/fragments/addToCartModal";
        }
    }

    @RequestMapping(value = "addToWishList.htm", method = { RequestMethod.GET, RequestMethod.POST })
    public String addToWishlist(@ModelAttribute AddToCartItems addToCartItems, BindingResult errors, ModelMap model, HttpServletRequest request) {
        return "redirect:success";
    }

    @RequestMapping(value = "viewCart.htm", params = "removeItemFromCart", method = { RequestMethod.GET, RequestMethod.POST })
    public String removeItem(@RequestParam long orderItemId, ModelMap model, HttpServletRequest request) {
        Order currentCartOrder = retrieveCartOrder(request, model);
        try {
            currentCartOrder = cartService.removeItemFromOrder(currentCartOrder.getId(), orderItemId);
        } catch (PricingException e) {
            model.addAttribute("error", "remove");
            LOG.error("An error occurred while removing an item from the cart: (" + orderItemId + ")", e);
        }
        return removeItemViewRedirect ? "redirect:" + removeItemView : removeItemView;
    }

    @RequestMapping(value = "beginCheckout.htm", method = RequestMethod.GET)
    public String beginCheckout(@ModelAttribute CartSummary cartSummary, BindingResult errors, @RequestParam(required = false) Boolean isStorePickup, ModelMap model, HttpServletRequest request) {
        String view = "error";
        if (!view.equals("error")) {
            model.addAttribute("isStorePickup", isStorePickup);
            if (SecurityContextHolder.getContext().getAuthentication() == null || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                model.addAttribute("nextStep", "checkout");
                return "login";
            }
        }
        return view;
    }

    @RequestMapping(value = "viewCart.htm", params = "updateItemQuantity", method = RequestMethod.POST)
    public String updateItemQuantity(@ModelAttribute(value = "cartSummary") CartSummary cartSummary, Errors errors, ModelMap model, HttpServletRequest request) throws PricingException {
        if (errors.hasErrors()) {
            model.addAttribute("cartSummary", cartSummary);
            return cartView;
        }
        Order currentCartOrder = retrieveCartOrder(request, model);
        List<OrderItem> orderItems = currentCartOrder.getOrderItems();
        for (CartOrderItem cartOrderItem : cartSummary.getRows()) {
            OrderItem orderItem = (OrderItem) CollectionUtils.find(orderItems, new BeanPropertyValueEqualsPredicate("id", cartOrderItem.getOrderItem().getId()));
            if (orderItem != null) {
                if (cartOrderItem.getQuantity() > 0) {
                    orderItem.setQuantity(cartOrderItem.getQuantity());
                    try {
                        cartService.updateItemInOrder(currentCartOrder, orderItem);
                    } catch (ItemNotFoundException e) {
                        LOG.error("Item not found in order: (" + orderItem.getId() + ")", e);
                    } catch (PricingException e) {
                        LOG.error("Unable to price the order: (" + currentCartOrder.getId() + ")", e);
                    }
                } else {
                    try {
                        cartService.removeItemFromOrder(currentCartOrder, orderItem);
                    } catch (Exception e) {
                        LOG.error("Unable to remove item from the order: (" + currentCartOrder.getId() + ")");
                    }
                }
            }
        }
        return cartView;
    }

    @RequestMapping(params = "checkout", method = RequestMethod.POST)
    public String checkout(@ModelAttribute(value = "cartSummary") CartSummary cartSummary, Errors errors, ModelMap model, HttpServletRequest request) throws PricingException {
        Order currentCartOrder = retrieveCartOrder(request, model);
        updateFulfillmentGroups(cartSummary, currentCartOrder);
        return "redirect:/checkout/checkout.htm";
    }

    @RequestMapping(params = "updateShipping", method = RequestMethod.POST)
    public String updateShipping(@ModelAttribute(value = "cartSummary") CartSummary cartSummary, ModelMap model, HttpServletRequest request) throws PricingException {
        Order currentCartOrder = retrieveCartOrder(request, model);
        model.addAttribute("currentCartOrder", updateFulfillmentGroups(cartSummary, currentCartOrder));
        model.addAttribute("cartSummary", cartSummary);
        return cartView;
    }

    private Order updateFulfillmentGroups(CartSummary cartSummary, Order currentCartOrder) throws PricingException {
        cartService.removeAllFulfillmentGroupsFromOrder(currentCartOrder, false);
        cartService.addFulfillmentGroupToOrder(currentCartOrder, cartSummary.getFulfillmentGroup());
        return cartService.save(currentCartOrder, true);
    }

    @RequestMapping(value = "viewCart.htm", method = RequestMethod.GET)
    public String viewCart(ModelMap model, HttpServletRequest request) throws PricingException {
        LOG.debug("Processing View Cart!");
        Order cart = retrieveCartOrder(request, model);
        CartSummary cartSummary = new CartSummary();
        for (OrderItem orderItem : cart.getOrderItems()) {
            CartOrderItem cartOrderItem = new CartOrderItem();
            cartOrderItem.setOrderItem(orderItem);
            cartOrderItem.setQuantity(orderItem.getQuantity());
            cartSummary.getRows().add(cartOrderItem);
        }
        if ((cart.getFulfillmentGroups() != null) && (cart.getFulfillmentGroups().isEmpty() == false)) {
            String cartShippingMethod = cart.getFulfillmentGroups().get(0).getMethod();
            if (cartShippingMethod != null) {
                if (cartShippingMethod.equals("standard")) {
                    cartSummary = createFulfillmentGroup(cartSummary, "standard", cart);
                } else if (cartShippingMethod.equals("expedited")) {
                    cartSummary = createFulfillmentGroup(cartSummary, "expedited", cart);
                }
            }
        }
        updateFulfillmentGroups(cartSummary, cart);
        model.addAttribute("cartSummary", cartSummary);
        return cartViewRedirect ? "redirect:" + cartView : cartView;
    }

    private CartSummary createFulfillmentGroup(CartSummary cartSummary, String shippingMethod, Order cart) {
        FulfillmentGroup fulfillmentGroup = new FulfillmentGroupImpl();
        fulfillmentGroup.setMethod(shippingMethod);
        fulfillmentGroup.setOrder(cart);
        cartSummary.setFulfillmentGroup(fulfillmentGroup);
        return cartSummary;
    }

    @ModelAttribute("fulfillmentGroups")
    public List<FulfillmentGroup> initFulfillmentGroups() {
        List<FulfillmentGroup> fulfillmentGroups = new ArrayList<FulfillmentGroup>();
        FulfillmentGroup standardGroup = new FulfillmentGroupImpl();
        FulfillmentGroup expeditedGroup = new FulfillmentGroupImpl();
        standardGroup.setMethod("standard");
        expeditedGroup.setMethod("expedited");
        fulfillmentGroups.add(standardGroup);
        fulfillmentGroups.add(expeditedGroup);
        return fulfillmentGroups;
    }

    protected Order retrieveCartOrder(HttpServletRequest request, ModelMap model) {
        Customer currentCustomer = customerState.getCustomer(request);
        Order currentCartOrder = null;
        if (currentCustomer != null) {
            currentCartOrder = cartService.findCartForCustomer(currentCustomer);
            if (currentCartOrder == null) {
                currentCartOrder = cartService.createNewCartForCustomer(currentCustomer);
            }
        }
        model.addAttribute("currentCartOrder", currentCartOrder);
        return currentCartOrder;
    }
}
