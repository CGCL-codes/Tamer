package net.javacrumbs.fluentapi3;

public class StaticFactory {

    public static ResponseActions expect(RequestMatcher matcher) {
        MockInternal mock = new MockInternal();
        mock.andExpect(matcher);
        return mock;
    }

    public static void verify() {
    }

    public static RequestMatcher value(String value) {
        return new RequestMatcher() {

            public void match(Object someParams) {
            }
        };
    }

    public static ResponseCallback withValue(String value) {
        return new ResponseCallback() {

            public void doWithResponse(Object someParams) {
            }
        };
    }

    public static RequestMatcher header(String value) {
        return new RequestMatcher() {

            public void match(Object someParams) {
            }
        };
    }

    public static ResponseCallback withError(String value) {
        return new ResponseCallback() {

            public void doWithResponse(Object someParams) {
            }
        };
    }
}
