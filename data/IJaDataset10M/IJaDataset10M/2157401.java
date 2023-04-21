package com.google.solarchallenge.shared.dtos;

import com.google.solarchallenge.shared.LoginCategory;

/**
 * DTO for UserAccount.
 *
 * @author Arjun Satyapal
 */
public class UserAccountDto implements AbstractDto {

    private String key;

    private String firstName;

    private String lastName;

    private String email;

    private LoginCategory loginCategory;

    private boolean loginAllowed;

    @Override
    public StringBuilder getStringBuilder() {
        StringBuilder builder = new StringBuilder("ApproverDto[").append("key:").append(key).append(", firstName:").append(firstName).append(", lastName:").append(lastName).append(", email:").append(email).append(", loginCategory:").append(loginCategory).append(", isLoginAllowed:").append(loginAllowed).append("].");
        return builder;
    }

    @Override
    public String toString() {
        return getStringBuilder().toString();
    }

    private UserAccountDto() {
    }

    @Override
    public String validate() {
        StringBuilder builder = new StringBuilder("");
        if (firstName.isEmpty()) {
            builder.append("firstName is empty.\n");
        }
        if (lastName.isEmpty()) {
            builder.append("lastName is empty.\n");
        }
        if (email.isEmpty()) {
            builder.append("email is empty.\n");
        }
        return builder.toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLoginAllowed() {
        return loginAllowed;
    }

    public LoginCategory getLoginCategory() {
        return loginCategory;
    }

    public static class Builder {

        private String key;

        private String firstName;

        private String lastName;

        private String email;

        private LoginCategory loginCategory;

        private boolean isLoginAllowed;

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setIsLoginAllowed(boolean isLoginAllowed) {
            this.isLoginAllowed = isLoginAllowed;
            return this;
        }

        public Builder setLoginCategory(LoginCategory loginCategory) {
            this.loginCategory = loginCategory;
            return this;
        }

        public UserAccountDto build() {
            UserAccountDto dto = new UserAccountDto();
            dto.key = key;
            dto.firstName = firstName;
            dto.lastName = lastName;
            dto.email = email;
            dto.loginCategory = loginCategory;
            dto.loginAllowed = isLoginAllowed;
            dto.validate();
            return dto;
        }
    }
}
