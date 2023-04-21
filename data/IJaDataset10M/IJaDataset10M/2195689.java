package com.devunion.salon.ajax.handler;

import com.devunion.salon.persistence.Customer;
import com.devunion.salon.persistence.GiftCard;
import com.devunion.salon.persistence.Product;
import com.devunion.salon.persistence.Service;
import com.devunion.salon.persistence.dao.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.util.MessageResources;
import org.springframework.beans.factory.InitializingBean;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxActionEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;
import org.springmodules.xt.ajax.action.RemoveContentAction;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.SetAttributeAction;
import org.springmodules.xt.ajax.component.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author Timoshenko Alexander
 */
public class SimpleHandler extends AbstractAjaxHandler implements InitializingBean {

    private Service2EmployeeDao service2EmployeeDao;

    private CustomerDao customerDao;

    private GiftCardDao giftCardDao;

    private DecimalFormat decimalFormat;

    private ServiceDao serviceDao;

    private ProductDao productDao;

    protected MessageResources resources = MessageResources.getMessageResources("ApplicationResources");

    private List<String> payments = new ArrayList<String>();

    public void setService2EmployeeDao(Service2EmployeeDao service2EmployeeDao) {
        this.service2EmployeeDao = service2EmployeeDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void setGiftCardDao(GiftCardDao giftCardDao) {
        this.giftCardDao = giftCardDao;
    }

    public void afterPropertiesSet() throws Exception {
        decimalFormat = new DecimalFormat(MessageResources.getMessageResources("ApplicationResources").getMessage("usd.money.format"));
        payments.add(resources.getMessage("modal.add.transaction.payment.amex"));
        payments.add(resources.getMessage("modal.add.transaction.payment.visa"));
        payments.add(resources.getMessage("modal.add.transaction.payment.mastercard"));
        payments.add(resources.getMessage("modal.add.transaction.payment.cheque"));
        payments.add(resources.getMessage("modal.add.transaction.payment.cache"));
    }

    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public AjaxResponse load(AjaxActionEvent event) {
        List<Component> result = new ArrayList<Component>();
        String name = event.getElementName();
        String key = event.getHttpRequest().getParameter(name);
        Collection<Service> services = service2EmployeeDao.getEmployeeServices(Long.valueOf(key));
        for (Service service : services) {
            result.add(new Option(service, "id", "name"));
        }
        ReplaceContentAction action = new ReplaceContentAction("services", result);
        AjaxResponse response = new AjaxResponseImpl();
        response.addAction(action);
        return response;
    }

    public AjaxResponse updateCustomer(AjaxActionEvent event) {
        String customerId = event.getParameters().get("customerId");
        Customer customer = customerDao.getByKey(Long.valueOf(customerId));
        AjaxResponse response = new AjaxResponseImpl();
        response.addAction(new SetAttributeAction("customerId", "value", customerId));
        response.addAction(new SetAttributeAction("customerFirstName", "value", customer.getFirstName()));
        response.addAction(new SetAttributeAction("customerLastName", "value", customer.getLastName()));
        response.addAction(new SetAttributeAction("customerPhone", "value", customer.getPhone()));
        response.addAction(new SetAttributeAction("customerCellPhone", "value", customer.getCellPhone()));
        response.addAction(new SetAttributeAction("customerEmail", "value", customer.getEmail()));
        return response;
    }

    public AjaxResponse getGiftcardAmount(AjaxActionEvent event) {
        String giftcardNumber = event.getHttpRequest().getParameter("giftcard");
        GiftCard giftCard = giftCardDao.getCustomerGiftcard(giftcardNumber);
        AjaxResponse response = new AjaxResponseImpl();
        if (giftCard != null) {
            response.addAction(new ReplaceContentAction("giftcardAmount", new SimpleText(decimalFormat.format(giftCard.getAmount()))));
        } else {
            String startRedFont = resources.getMessage("font.color.red.begin");
            String endRedFont = resources.getMessage("font.color.red.end");
            if (StringUtils.isBlank(giftcardNumber)) {
                response.addAction(new ReplaceContentAction("giftcardAmount", new SimpleText(startRedFont + resources.getMessage("errors.required", resources.getMessage("payment.table.giftcard")) + endRedFont)));
            } else {
                response.addAction(new ReplaceContentAction("giftcardAmount", new SimpleText(startRedFont + resources.getMessage("errors.giftcard", giftcardNumber) + endRedFont)));
            }
        }
        return response;
    }

    public AjaxResponse autocompleteService(AjaxActionEvent event) {
        String employeeKey = event.getParameters().get("employeeId");
        String serviceName = event.getParameters().get("service");
        List<Component> result = new ArrayList<Component>();
        Collection<Service> services;
        AjaxResponse response = new AjaxResponseImpl();
        if (!StringUtils.isBlank(employeeKey)) {
            if (!StringUtils.isBlank(serviceName)) {
                services = serviceDao.getServiceByCriteria(serviceName, employeeKey);
            } else {
                services = service2EmployeeDao.getEmployeeServices(Long.valueOf(employeeKey));
            }
            for (Service service : services) {
                result.add(new Option(service, "id", "name"));
            }
            ReplaceContentAction action = new ReplaceContentAction("services", result);
            response.addAction(action);
        }
        return response;
    }

    public AjaxResponse autocompleteProduct(AjaxActionEvent event) {
        String employeeKey = event.getParameters().get("employeeId");
        String productName = event.getParameters().get("product");
        List<Component> result = new ArrayList<Component>();
        AjaxResponse response = new AjaxResponseImpl();
        if (!StringUtils.isBlank(employeeKey)) {
            Collection<Product> products = productDao.getProductByCriteria(productName, employeeKey);
            for (Product product : products) {
                result.add(new Option(product, "id", "name"));
            }
            ReplaceContentAction action = new ReplaceContentAction("products", result);
            response.addAction(action);
        }
        return response;
    }

    public AjaxResponse getSecondPayment(AjaxActionEvent event) {
        Map<String, String[]> parameters = event.getHttpRequest().getParameterMap();
        List<String> errors = new ArrayList<String>();
        Float totalPrice = Float.valueOf(parameters.get("totalPriceAndTaxes")[0]);
        Boolean checked = Boolean.valueOf(event.getParameters().get("checked"));
        AjaxResponse response = new AjaxResponseImpl();
        if (checked != null && checked == Boolean.TRUE) {
            BigDecimal secondPaymentAmount = null;
            String[] discounts = parameters.get("discount");
            if (discounts == null || discounts[0] == null || StringUtils.isBlank(discounts[0])) {
                errors.add(resources.getMessage("errors.required", resources.getMessage("payment.table.discount")));
            } else {
                String discount = discounts[0];
                validateNumber(resources.getMessage("payment.table.discount"), discount, 0f, 99.99f, errors);
            }
            String[] cashes = parameters.get("amount");
            if (cashes != null && cashes.length > 0) {
                if (cashes[0] == null || StringUtils.isBlank(cashes[0])) {
                    errors.add(resources.getMessage("errors.required", resources.getMessage("payment.table.cash")));
                } else {
                    String cash = cashes[0];
                    if (validateNumber(resources.getMessage("payment.table.cash"), cash, 0f, totalPrice, errors)) {
                        if (errors.isEmpty()) {
                            Float discount = Float.valueOf(discounts[0]);
                            secondPaymentAmount = new BigDecimal(totalPrice - Float.valueOf(cash)).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                }
            }
            if (!errors.isEmpty()) {
                response.addAction(new ReplaceContentAction("validatePaymentMethodID", new SimpleText(buildErrorResponse(errors))));
                return response;
            } else {
                response.addAction(new RemoveContentAction("validatePaymentMethodID"));
            }
            Table table = new Table(null, new ArrayList<TableRow>());
            TableRow first = new TableRow();
            first.addTableData(new TableData(new SimpleText(resources.getMessage("payment.second.method.payment"))));
            Select select = new Select("secondPaymentMethod");
            for (String payment : payments) {
                select.addOption(new Option(payment, payment));
            }
            first.addTableData(new TableData(select));
            table.addTableRow(first);
            TableRow second = new TableRow();
            second.addTableData(new TableData(new SimpleText(resources.getMessage("payment.second.method.total"))));
            InputField field = new InputField("secondPaymentAmount", (secondPaymentAmount != null ? secondPaymentAmount.toString() : "0"), InputField.InputType.TEXT);
            field.addAttribute("id", "secondPaymentAmount");
            second.addTableData(new TableData(field));
            table.addTableRow(second);
            response.addAction(new ReplaceContentAction("secondPaymentMethodID", table));
        } else {
            response.addAction(new RemoveContentAction("secondPaymentMethodID"));
        }
        return response;
    }

    public AjaxResponse validateGiftcardForm(AjaxActionEvent event) {
        AjaxResponse response = new AjaxResponseImpl();
        Map<String, String[]> parameters = event.getHttpRequest().getParameterMap();
        List<String> errors = new ArrayList<String>();
        String[] amounts = parameters.get("amount");
        if (amounts == null || amounts[0] == null || StringUtils.isBlank(amounts[0])) {
            errors.add(resources.getMessage("errors.required", resources.getMessage("payment.table.discount")));
        } else {
            String amount = amounts[0];
            validateNumber(resources.getMessage("payment.table.discount"), amount, 0.01f, null, errors);
        }
        if (errors.size() > 0) {
            response.addAction(new ReplaceContentAction("validateErrorsID", new SimpleText(buildErrorResponse(errors))));
        } else {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("formName", "modalGiftcardForm");
            params.put("submittedValue", resources.getMessage("modal.giftcard.save"));
            response.addAction(new ExecuteJavascriptFunctionAction("submitForm", params));
        }
        return response;
    }

    public AjaxResponse calculateChange(AjaxActionEvent event) {
        AjaxResponse response = new AjaxResponseImpl();
        Map<String, String[]> parameters = event.getHttpRequest().getParameterMap();
        List<String> errors = new ArrayList<String>();
        Float totalPrice = Float.valueOf(parameters.get("totalPriceAndTaxes")[0]);
        String[] discounts = parameters.get("discount");
        if (discounts == null || discounts[0] == null || StringUtils.isBlank(discounts[0])) {
            errors.add(resources.getMessage("errors.required", resources.getMessage("payment.table.discount")));
        } else {
            String discount = discounts[0];
            validateNumber(resources.getMessage("payment.table.discount"), discount, 0f, 99.99f, errors);
        }
        String[] cashes = parameters.get("amount");
        if (cashes != null && cashes.length > 0) {
            if (cashes[0] == null || StringUtils.isBlank(cashes[0])) {
                errors.add(resources.getMessage("errors.required", resources.getMessage("payment.table.cash")));
            } else {
                String cash = cashes[0];
                if (validateNumber(resources.getMessage("payment.table.cash"), cash, totalPrice, null, errors)) {
                    if (errors.isEmpty()) {
                        Float discount = Float.valueOf(discounts[0]);
                        BigDecimal changes = new BigDecimal(Float.valueOf(cash) - (totalPrice - (totalPrice * discount / 100))).setScale(2, RoundingMode.HALF_UP);
                        response.addAction(new ReplaceContentAction("cachChange", new SimpleText(decimalFormat.format(changes))));
                    }
                }
            }
        }
        if (errors.size() > 0) {
            response.addAction(new ReplaceContentAction("validatePaymentMethodID", new SimpleText(buildErrorResponse(errors))));
        } else {
            response.addAction(new RemoveContentAction("validatePaymentMethodID"));
        }
        return response;
    }

    public AjaxResponse validatePaymentForm(AjaxActionEvent event) {
        AjaxResponse response = new AjaxResponseImpl();
        Map<String, String[]> parameters = event.getHttpRequest().getParameterMap();
        List<String> errors = new ArrayList<String>();
        Float totalPrice = Float.valueOf(parameters.get("totalPriceAndTaxes")[0]);
        Boolean isChecked = Boolean.FALSE;
        String[] secondPaymentMethod = parameters.get("checkPayment");
        if (secondPaymentMethod != null && secondPaymentMethod.length > 0 && BooleanUtils.toBoolean(secondPaymentMethod[0])) {
            isChecked = Boolean.TRUE;
        }
        String[] discounts = parameters.get("discount");
        if (discounts == null || discounts[0] == null || StringUtils.isBlank(discounts[0])) {
            errors.add(resources.getMessage("errors.required", resources.getMessage("payment.table.discount")));
        } else {
            String discount = discounts[0];
            validateNumber(resources.getMessage("payment.table.discount"), discount, 0f, 99.99f, errors);
        }
        if (isChecked == Boolean.TRUE) {
            String[] cashes = parameters.get("amount");
            if (cashes != null && cashes.length > 0) {
                if (cashes[0] == null || StringUtils.isBlank(cashes[0])) {
                    errors.add(resources.getMessage("errors.required", resources.getMessage("payment.table.cash")));
                } else {
                    String cash = cashes[0];
                    if (validateNumber(resources.getMessage("payment.table.cash"), cash, 0f, totalPrice, errors)) {
                        if (errors.isEmpty()) {
                            Float discount = Float.valueOf(discounts[0]);
                            BigDecimal changes = new BigDecimal(Float.valueOf(cash) - (totalPrice - (totalPrice * discount / 100))).setScale(2, RoundingMode.HALF_UP);
                            response.addAction(new ReplaceContentAction("cachChange", new SimpleText(decimalFormat.format(changes))));
                        }
                    }
                }
            }
        }
        String[] giftcards = parameters.get("giftcard");
        if (giftcards != null && giftcards.length > 0) {
            if (giftcards[0] == null || StringUtils.isEmpty(giftcards[0])) {
                errors.add(resources.getMessage("errors.required", resources.getMessage("payment.table.giftcard")));
            } else {
                String giftcard = giftcards[0];
                validateGiftcard(resources.getMessage("payment.table.giftcard.amount"), giftcard, totalPrice, null, errors);
            }
        }
        if (isChecked == Boolean.TRUE) {
            String[] priceParameter = parameters.get("price");
            if (priceParameter != null && priceParameter.length > 0) {
                String price = priceParameter[0];
                Float taxes = Float.valueOf(parameters.get("totalTaxes")[0]);
                if (validateNumber(resources.getMessage("payment.table.price"), price, 0.01f, totalPrice - taxes, errors)) {
                    if (errors.isEmpty()) {
                        response.addAction(new SetAttributeAction("secondPaymentAmount", "value", new BigDecimal(totalPrice - Float.valueOf(price) - taxes).setScale(2, RoundingMode.HALF_UP).toString()));
                    }
                }
            }
        }
        if (errors.size() > 0) {
            response.addAction(new ReplaceContentAction("validatePaymentMethodID", new SimpleText(buildErrorResponse(errors))));
        } else {
            response.addAction(new RemoveContentAction("validatePaymentMethodID"));
            response.addAction(new ReplaceContentAction("submitPaymentID", new Button("submittedValue", resources.getMessage("payment.save.submit"), Button.ButtonType.SUBMIT, new SimpleText(resources.getMessage("payment.save.submit")))));
        }
        return response;
    }

    private Boolean validateNumber(String name, String value, Float from, Float to, List<String> errors) {
        Boolean result = Boolean.TRUE;
        if (NumberUtils.isNumber(value)) {
            Float number = Float.valueOf(value);
            if (from != null && to != null) {
                if (number < from || number > to) {
                    errors.add(resources.getMessage("errors.range", name, new BigDecimal(from).setScale(2, RoundingMode.HALF_UP).toString(), new BigDecimal(to).setScale(2, RoundingMode.HALF_UP).toString()));
                    result = Boolean.FALSE;
                }
            } else if (from != null) {
                if (number < from) {
                    errors.add(resources.getMessage("errors.min", name, new BigDecimal(from).setScale(2, RoundingMode.HALF_UP).toString()));
                    result = Boolean.FALSE;
                }
            } else if (to != null) {
                if (number > to) {
                    errors.add(resources.getMessage("errors.max", name, new BigDecimal(to).setScale(2, RoundingMode.HALF_UP).toString()));
                    result = Boolean.FALSE;
                }
            }
        } else {
            errors.add(resources.getMessage("errors.number", name));
            result = Boolean.FALSE;
        }
        return result;
    }

    private Boolean validateGiftcard(String name, String value, Float from, Float to, List<String> errors) {
        Boolean result = Boolean.TRUE;
        GiftCard giftCard = giftCardDao.getCustomerGiftcard(value);
        if (giftCard != null) {
            result = validateNumber(name, String.valueOf(giftCard.getAmount()), from, to, errors);
        } else {
            errors.add(resources.getMessage("errors.giftcard", value));
        }
        return result;
    }

    public String buildErrorResponse(List<String> errors) {
        String startRedFont = resources.getMessage("font.color.red.begin");
        String endRedFont = resources.getMessage("font.color.red.end");
        StringBuilder response = new StringBuilder("<ul>");
        for (String error : errors) {
            response.append(startRedFont).append("<li>").append(error).append("</li>").append(endRedFont);
        }
        response.append("</ul>");
        return response.toString();
    }
}
