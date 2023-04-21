package thymeleafexamples.gtvg.business.services;

import java.util.List;
import thymeleafexamples.gtvg.business.entities.Order;
import thymeleafexamples.gtvg.business.entities.repositories.OrderRepository;

public class OrderService {

    public OrderService() {
        super();
    }

    public List<Order> findAll() {
        return OrderRepository.getInstance().findAll();
    }

    public Order findById(final Integer id) {
        return OrderRepository.getInstance().findById(id);
    }

    public List<Order> findByCustomerId(final Integer customerId) {
        return OrderRepository.getInstance().findByCustomerId(customerId);
    }
}
