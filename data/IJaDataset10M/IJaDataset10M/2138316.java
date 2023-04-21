package model;

import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import com.LoginBean;
import com.ProductManager;

@ManagedBean
@SessionScoped
public class Order {

    private String phone;

    private String mail;

    private String city;

    private Long orderId;

    private Set<Product> product;

    private String info;

    private String extraInfo;

    @ManagedProperty(value = "#{productManager}")
    private ProductManager productManager;

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

    public Order() {
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Set<Product> getProduct() {
        return product;
    }

    public void setProduct(Set<Product> product) {
        this.product = product;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        if (loginBean.isAuthenticated()) {
            return productManager.getUserMail(loginBean.getUserDetails().getUsername());
        }
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCity() {
        if (loginBean.isAuthenticated()) {
            return productManager.getUserCity(loginBean.getUserDetails().getUsername());
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void resetForm() {
        this.city = "";
        this.mail = "";
        this.phone = "";
    }
}
