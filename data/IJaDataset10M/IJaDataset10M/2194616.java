package com.isdinvestments.cam.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.isdinvestments.cam.domain.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
